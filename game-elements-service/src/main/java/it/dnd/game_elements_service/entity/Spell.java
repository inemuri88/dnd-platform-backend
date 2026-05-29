package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.enumerate.*;
import it.dnd.game_elements_service.entity.utilityspell.SpellLevelEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Rappresenta un incantesimo (Spell) del sistema D&D 3.5.
 *
 * Un incantesimo ha una scheda tecnica fissa (scuola, componenti, gittata…)
 * e può comparire in più liste di incantesimi (Wizard, Cleric, Druid…)
 * a livelli diversi. Questa relazione N:M con livello è gestita da
 * {@link SpellLevelEntry}, che vive nella cartella utilityspell/.
 *
 * STRUTTURA DATI:
 *
 * 1. SCUOLA / SOTTOSCUOLA / DESCRITTORI
 *    Ogni spell appartiene a una sola scuola (SpellSchool) e opzionalmente
 *    a una sottoscuola (SpellSubschool). I descrittori (SpellDescriptor)
 *    sono un insieme: un incantesimo può avere più descrittori
 *    (es. [FIRE, EVOCATION] oppure [MIND_AFFECTING, FEAR]).
 *    Sono memorizzati con @ElementCollection → tabella "spell_descriptors".
 *
 * 2. COMPONENTI
 *    Le componenti (V, S, M, F, DF, XP) sono salvate con @ElementCollection
 *    → tabella "spell_components". I campi materialComponents e focusComponents
 *    contengono il testo descrittivo che accompagna M/F (es. "a pinch of salt").
 *
 * 3. TARGET / AREA / EFFECT
 *    In D&D 3.5 ogni spell usa UNO SOLO tra target, area ed effect.
 *    Tutti e tre sono nullable: il service deve valorizzare solo quello corretto.
 *
 * 4. LIVELLI PER LISTA
 *    La relazione Spell → SpellList è N:M con un attributo (il livello):
 *    lo stesso spell può essere livello 3 per il Wizard e livello 4 per il Sorcerer.
 *    Si usa una entity ponte SpellLevelEntry invece di una @ManyToMany semplice
 *    esattamente per poter trasportare quell'attributo extra.
 *
 * PATTERN SEGUITO (uguale a ClassCharacter / Race):
 * - @ElementCollection per insiemi di valori scalari/enum senza identità propria.
 * - @OneToMany con cascade ALL + orphanRemoval per le relazioni "figlie".
 * - FetchType.LAZY ovunque: le liste vengono caricate solo quando servono.
 * - Nessuna estensione di CreationUpdate: dato statico di sistema.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Spell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome ufficiale dell'incantesimo (es. "Fireball", "Cure Light Wounds")
    @Column(nullable = false, unique = true)
    private String name;

    // -------------------------------------------------------------------------
    // Scuola / Sottoscuola / Descrittori
    // -------------------------------------------------------------------------

    // Scuola di magia obbligatoria (es. EVOCATION, CONJURATION, ILLUSION)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpellSchool school;

    // Sottoscuola opzionale (es. CREATION per Conjuration, CHARM per Enchantment)
    @Enumerated(EnumType.STRING)
    private SpellSubschool subschool;

    // Descrittori opzionali: set perché un incantesimo può averne più di uno.
    // Genera la tabella "spell_descriptors" con colonne (spell_id, descriptor).
    @ElementCollection
    @CollectionTable(name = "spell_descriptors", joinColumns = @JoinColumn(name = "spell_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "descriptor")
    private Set<SpellDescriptor> descriptors = new HashSet<>();

    // -------------------------------------------------------------------------
    // Componenti
    // -------------------------------------------------------------------------

    // Componenti richieste per il lancio (V, S, M, F, DF, XP).
    // Genera la tabella "spell_components" con colonne (spell_id, component).
    @ElementCollection
    @CollectionTable(name = "spell_components", joinColumns = @JoinColumn(name = "spell_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "component")
    private Set<SpellComponent> components = new HashSet<>();

    // Testo descrittivo per la componente Materiale (M): cosa serve fisicamente.
    // Valorizzato solo se components contiene M.
    @Column(length = 500)
    private String materialComponents;

    // Testo descrittivo per la componente Focus (F): l'oggetto da avere in mano.
    // Valorizzato solo se components contiene F o DF.
    @Column(length = 500)
    private String focusComponents;

    // -------------------------------------------------------------------------
    // Parametri di lancio
    // -------------------------------------------------------------------------

    // Tempo di lancio secondo le regole D&D 3.5
    // es. "1 standard action", "1 full-round action", "1 minute"
    @Column(nullable = false)
    private String castingTime;

    // Gittata dell'effetto. In D&D 3.5 ha categorie standard ma anche valori
    // unici, quindi si usa String per flessibilità.
    // es. "Personal", "Touch", "Close (7.5 m + 1.5 m/2 levels)", "Long"
    private String range;

    // --- Solo UNO tra i tre seguenti viene valorizzato per ogni spell ---

    // Target: individui specifici colpiti (es. "One creature")
    @Column(length = 500)
    private String target;

    // Area: zona geometrica (es. "6-m-radius burst")
    @Column(length = 500)
    private String area;

    // Effect: per spell che creano qualcosa (es. "Ray", "Wall of fire up to 6 m")
    @Column(length = 500)
    private String effect;

    // Durata dell'effetto. La "(D)" indica dismissible (il caster può terminarlo).
    // es. "Instantaneous", "1 round/level (D)", "Concentration, up to 1 round/level"
    private String duration;

    // Tiro salvezza consentito al bersaglio.
    // es. "Will negates", "Reflex half", "Fortitude partial", "None"
    private String savingThrow;

    // true = la Spell Resistance del bersaglio si applica.
    // null = non specificato / non pertinente (spell senza bersaglio).
    private Boolean spellResistance;

    // -------------------------------------------------------------------------
    // Testo descrittivo
    // -------------------------------------------------------------------------

    // Descrizione completa dell'effetto meccanico (e opzionale flavour text)
    @Column(length = 5000)
    private String description;

    // Libro sorgente (es. "PHB", "Spell Compendium")
    @Column(length = 200)
    private String sourceBook;

    // -------------------------------------------------------------------------
    // Livelli per lista incantesimi  →  entity ponte SpellLevelEntry
    // -------------------------------------------------------------------------

    // Ogni entry indica: "questo spell è livello X nella lista Y".
    // Es. Fireball → Wizard 3, Sorcerer 3.
    // Si usa un'entity ponte (non @ManyToMany) perché la relazione porta
    // l'attributo "spellLevel", che una @ManyToMany non può esprimere.
    @OneToMany(mappedBy = "spell", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpellLevelEntry> spellLevels = new ArrayList<>();
}