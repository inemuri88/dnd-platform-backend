package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.enumerate.*;
import it.dnd.game_elements_service.entity.proficiency.ArmorProficiency;
import it.dnd.game_elements_service.entity.proficiency.WeaponProficiency;
import it.dnd.game_elements_service.entity.utilityspell.SpellList;
import it.dnd.game_elements_service.entity.utilityspell.SpellsKnownRow;
import it.dnd.game_elements_service.entity.utilityspell.SpellsPerDayRow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Rappresenta una Classe del personaggio (ClassCharacter) in D&D 3.5.
 *
 * È l'entity più complessa del modulo. Ogni classe definisce la propria
 * progressione meccanica livello per livello: BaB, tiri salvezza, dadi vita,
 * punti abilità, capacità di classe, eventuali incantesimi.
 *
 * STRUTTURA PER SEZIONI:
 *
 * 1. IDENTIFICAZIONE
 *    "code" è la chiave tecnica stabile (es. "BARBARIAN") usata in seed data,
 *    migration e API senza dipendere dall'id auto-generato dal DB.
 *    "name" è il nome leggibile (es. "Barbarian").
 *
 * 2. PROGRESSIONI CORE (babProgression, fortSave, refSave, willSave, hitDie)
 *    Ogni classe ha progressioni fisse definite dal manuale:
 *      BonusBAB   → GOOD (+1/livello), AVERAGE (¾/livello), POOR (½/livello)
 *      Saving Throw → GOOD (buona, sale di ½ livello + 2), POOR (sale di ⅓ livello)
 *      HitDie     → dado vita per livello (D6, D8, D10, D12)
 *
 * 3. SISTEMA SKILL
 *    skillPointsPerLevel: punti abilità guadagnati per livello (es. 2 per Fighter, 8 per Rogue).
 *    startingSkillPointsMultiplier: al 1° livello si moltiplicano per 4 (regola standard).
 *    skillKeyAbility: quasi sempre INT, ma è esplicitato per non lasciare ambiguità.
 *    classSkills: le skill "di classe" hanno costo 1 punto per rank (le cross-class costano 2).
 *
 * 4. PROFICIENCY (armi e armature)
 *    @ManyToMany verso WeaponProficiency e ArmorProficiency.
 *    Queste entity sono condivise tra più classi (es. Fighter e Paladin hanno
 *    entrambi "Martial Weapons"), quindi @ManyToMany è la scelta corretta.
 *
 * 5. ALLINEAMENTI CONSENTITI
 *    @ManyToMany verso Alignment. Es. Paladin: solo LAWFUL_GOOD.
 *    Barbarian: tutto tranne LAWFUL_*.
 *
 * 6. CLASS FEATURES
 *    Non si usa @ManyToMany diretta verso ClassFeature perché la relazione
 *    porta attributi extra: il livello a cui si ottiene la feature e un
 *    eventuale valore numerico (es. Sneak Attack 2d6 al livello 3).
 *    Si usa quindi ClassFeatureGrant come entity ponte.
 *
 * 7. SPELLCASTING
 *    spellcaster: flag boolean per sapere rapidamente se la classe lancia incantesimi.
 *    castingType: PREPARED (Wizard, Cleric) o SPONTANEOUS (Sorcerer, Bard).
 *    castingAbility: INT (Wizard), WIS (Cleric/Druid), CHA (Sorcerer/Bard).
 *    spellList: la lista di incantesimi a cui ha accesso (entity SpellList).
 *    spellsPerDay: tabella di progressione degli slot per livello classe (SpellsPerDayRow).
 *    spellsKnown: tabella degli incantesimi conosciuti, usata solo dai caster spontanei.
 *
 * 8. PRESTIGE CLASS
 *    prestigeClass=true identifica le classi di prestigio che richiedono prerequisiti.
 *    I prerequisiti sono in ClassPrerequisite (stessa struttura di FeatPrerequisite).
 *    maxLevel è tipicamente 10 per le prestige class (contro 20 per le classi base).
 *
 * NON estende CreationUpdate: ClassCharacter è seed data di sistema,
 * gestito via migrazioni Liquibase/Flyway, non modificato dagli utenti.
 */
@Entity
@Table(name = "class_character")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ClassCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Chiave tecnica stabile (per DB, API, seed data). Non aggiornabile dopo la creazione.
    @Column(nullable = false, unique = true, updatable = false)
    private String code; // es. "BARBARIAN", "ROGUE"

    @Column(nullable = false)
    private String name; // es. "Barbarian"

    // -------------------------------------------------------------------------
    // Meta / fonte
    // -------------------------------------------------------------------------

    @Column(length = 200)
    private String sourceBook; // es. "PHB", "Complete Warrior"

    @Column(length = 2000)
    private String flavorText;

    // -------------------------------------------------------------------------
    // Progressioni core
    // -------------------------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonusBAB babProgression;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonusSavingThrows fortSaveProgression;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonusSavingThrows refSaveProgression;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BonusSavingThrows willSaveProgression;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HitDie hitDie;

    // -------------------------------------------------------------------------
    // Sistema skill
    // -------------------------------------------------------------------------

    @Column(nullable = false)
    private Integer skillPointsPerLevel; // es. 2 (Fighter), 4 (Cleric), 6 (Bard), 8 (Rogue)

    // Al 1° livello i punti abilità si moltiplicano per questo valore (tipico: 4)
    @Column(nullable = false)
    private Integer startingSkillPointsMultiplier;

    // Quasi sempre INT, ma reso esplicito per non lasciare ambiguità al service
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbilityScore skillKeyAbility;

    // Skill "di classe": costo 1 punto/rank. Le altre sono cross-class (costo 2, max rank/2).
    @ManyToMany
    @JoinTable(
            name = "class_character_class_skill",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> classSkills = new HashSet<>();

    // -------------------------------------------------------------------------
    // Proficiency (armi e armature)
    // -------------------------------------------------------------------------

    // Entity condivise tra più classi → @ManyToMany corretta
    @ManyToMany
    @JoinTable(
            name = "class_character_weapon_proficiency",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "weapon_prof_id")
    )
    private Set<WeaponProficiency> weaponProficiencies = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "class_character_armor_proficiency",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "armor_prof_id")
    )
    private Set<ArmorProficiency> armorProficiencies = new HashSet<>();

    // -------------------------------------------------------------------------
    // Allineamenti consentiti
    // -------------------------------------------------------------------------

    // Es. Paladin: solo {LAWFUL_GOOD}. Barbarian: tutti esclusi quelli LAWFUL.
    @ManyToMany
    @JoinTable(
            name = "class_character_allowed_alignment",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "alignment_id")
    )
    private Set<Alignment> allowedAlignments = new HashSet<>();

    // -------------------------------------------------------------------------
    // Class Features
    // -------------------------------------------------------------------------

    // Entity ponte ClassFeatureGrant: trasporta livello e valore numerico
    // (es. Sneak Attack 2d6 al livello 3 → level=3, numericValue=2).
    // @OrderBy garantisce che la lista arrivi già ordinata per livello.
    @OneToMany(mappedBy = "classCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("level ASC")
    private List<ClassFeatureGrant> featureGrants = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Spellcasting
    // -------------------------------------------------------------------------

    // Flag di rapida verifica: evita di controllare castingType != null ovunque
    @Column(nullable = false)
    private boolean spellcaster;

    // PREPARED: Wizard (memorizza spell al mattino), Cleric, Druid
    // SPONTANEOUS: Sorcerer, Bard (sceglie quale spell lanciare al momento)
    @Enumerated(EnumType.STRING)
    private CastingType castingType;

    // Caratteristica che determina i DC dei saving throw e i bonus spell slot
    @Enumerated(EnumType.STRING)
    private AbilityScore castingAbility;

    // Lista di incantesimi a cui la classe ha accesso (Wizard List, Cleric List, ecc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spell_list_id")
    private SpellList spellList;

    // Slot incantesimi per livello classe: una riga per ogni livello della classe (1–20),
    // con le colonne level0Spells…level9Spells. Ordinata per classLevel.
    @OneToMany(mappedBy = "classCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("classLevel ASC")
    private List<SpellsPerDayRow> spellsPerDay = new ArrayList<>();

    // Incantesimi conosciuti: usata solo per i caster spontanei (Sorcerer, Bard).
    // Per i caster preparati questa lista è vuota (conoscono tutti gli spell nella lista).
    @OneToMany(mappedBy = "classCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("classLevel ASC")
    private List<SpellsKnownRow> spellsKnown = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Prestige class / Requisiti
    // -------------------------------------------------------------------------

    // Prerequisiti di accesso alla classe (solo per prestige class).
    // Stessa struttura type/value/numericValue usata in FeatPrerequisite.
    @OneToMany(mappedBy = "classCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassPrerequisite> prerequisites = new ArrayList<>();

    // Lato inverso della relazione @ManyToMany con Race (lato owning in Race.classes)
    @ManyToMany(mappedBy = "classes")
    private Set<Race> races = new HashSet<>();

    // 20 per classi base, 10 per la maggior parte delle prestige class
    @Column(nullable = false)
    private Integer maxLevel = 20;

    @Column(nullable = false)
    private boolean prestigeClass;

    @Column(length = 2000)
    private String notes;
}