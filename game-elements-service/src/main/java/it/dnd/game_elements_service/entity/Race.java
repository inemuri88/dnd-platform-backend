package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import it.dnd.game_elements_service.entity.enumerate.AbilityScore;
import it.dnd.game_elements_service.entity.enumerate.CreatureSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Rappresenta una Razza giocabile (Race) in D&D 3.5.
 *
 * In D&D 3.5 la razza determina: taglia, velocità di movimento, modificatori
 * alle caratteristiche, capacità speciali razziali, bonus/malus alle skill,
 * lingue automatiche e bonus, e le classi "favorite" (preferred class).
 *
 * STRUTTURA DEI DATI:
 *
 * 1. CAPACITÀ SPECIALI RAZZIALI (racialSpecialSkills)
 *    Map<String, String>: nome della capacità → descrizione testuale.
 *    Es. "Darkvision" → "Can see in the dark up to 18 meters."
 *    Usato @ElementCollection perché le capacità non sono entity autonome:
 *    appartengono solo a questa razza e non hanno id proprio.
 *    Genera la tabella "race_racial_skills" (race_id, special_skill_name, description).
 *    NOTA: Hibernate gestisce gli update dell'ElementCollection eliminando
 *    e ricreando tutte le righe della razza aggiornata. Accettabile finché
 *    il numero di capacità per razza è contenuto (< 20 righe).
 *
 * 2. MODIFICATORI ALLE CARATTERISTICHE (abilityModifiers)
 *    Map<AbilityScore, Integer>: caratteristica → valore del modificatore (positivo o negativo).
 *    Es. AbilityScore.STRENGTH → +2, AbilityScore.INTELLIGENCE → -2.
 *    Usa EnumMap per garantire ordine stabile e performance ottimale con chiavi enum.
 *    Genera la tabella "race_ability_mod" (race_id, ability_score, modifier).
 *
 * 3. BONUS/MALUS ALLE SKILL (racialBonusOrMalusSkills)
 *    Map<String, Integer>: nome della skill → bonus (positivo) o malus (negativo).
 *    Es. "Listen" → +2, "Search" → +2.
 *    Si usa il nome String invece dell'entity Skill per semplicità: queste sono
 *    regole fisse del manuale, non richiedono validazione relazionale a DB.
 *
 * 4. CLASSI FAVORITE (classes)
 *    @ManyToMany verso ClassCharacter: in D&D 3.5 ogni razza ha una o più classi
 *    "favorite" che non generano penalità XP multiclasse. La join table si chiama
 *    "prefer_classes_races". È il lato owning della relazione (ha la @JoinTable).
 *
 * 5. LINGUE → gestite tramite {@link it.dnd.game_elements_service.entity.relation.RaceLanguage}
 *    (entity ponte Race + Language + tipo AUTOMATIC/BONUS).
 *
 * 6. SOTTORAZE (parent / subRaces)
 *    In D&D 3.5 molte razze hanno sottovarianti con statistiche diverse:
 *    Es. Elf → High Elf, Wood Elf, Drow, Gray Elf, Wild Elf.
 *    Si usa una relazione auto-referenziale: la sottoRaza punta alla razza
 *    madre tramite "parent". La razza madre vede le sue sottoraze in "subRaces".
 *    Le sottoraze sono entity Race a tutti gli effetti: hanno i propri
 *    abilityModifiers, racialSpecialSkills, speed, ecc. che sovrascrivono o
 *    integrano quelli della razza padre. La logica di merge è nel service.
 *    Una Race con parent=null è una razza base; con parent valorizzato è una sottoRaza.
 *
 * Estende CreationUpdate: le razze sono dati curati dall'amministratore che
 * possono essere corretti nel tempo (createdAt + updatedAt tracciati).
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Race extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Velocità di movimento base in metri (es. 9.0 per la maggior parte, 6.0 per nani)
    @Column(nullable = false)
    private Double speed;

    // Taglia della creatura: influenza CA, attacco, gittata, Grapple, ecc.
    // FIX: aggiunto @Enumerated(EnumType.STRING). Prima mancava del tutto, quindi
    // JPA applicava il default EnumType.ORDINAL, salvando la taglia come intero
    // (0,1,2…). Era fragile (riordinare l'enum CreatureSize avrebbe corrotto i dati
    // esistenti) e incoerente con tutti gli altri enum del progetto, mappati come STRING.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreatureSize size;

    /*
     * TODO: Se nella descrizione di una capacità speciale c'è una distanza
     * (es. "Darkvision 18 m."), il service la parserà con un metodo apposito.
     */
    @ElementCollection
    @CollectionTable(
            name = "race_racial_skills",
            joinColumns = @JoinColumn(name = "race_id")
    )
    @MapKeyColumn(name = "special_skill_name")
    @Column(name = "description")
    private Map<String, String> racialSpecialSkills = new HashMap<>();

    // Modificatori razziali alle 6 caratteristiche. Chiave = caratteristica, valore = delta.
    // EnumMap mantiene l'ordine di dichiarazione dell'enum (STR, DEX, CON, INT, WIS, CHA).
    @ElementCollection
    @CollectionTable(
            name = "race_ability_mod",
            joinColumns = @JoinColumn(name = "race_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "ability_score")
    @Column(name = "modifier", nullable = false)
    private Map<AbilityScore, Integer> abilityModifiers = new EnumMap<>(AbilityScore.class);

    // Classi favorite: non generano penalità XP multiclasse per questa razza.
    // Lato owning della relazione @ManyToMany (possiede la @JoinTable).
    // FIX: corretti i nomi delle colonne della join table, prima invertiti.
    // joinColumns è il lato owning (Race), inverseJoinColumns è il target (ClassCharacter):
    // prima la FK verso Race si chiamava "class" e quella verso ClassCharacter "race"
    // (scambiate e fuorvianti a schema; "class" è anche un nome di colonna problematico).
    // Ora sono race_id (owning = Race) e class_id (target = ClassCharacter).
    @ManyToMany
    @JoinTable(
            name = "prefer_classes_races",
            joinColumns = @JoinColumn(name = "race_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<ClassCharacter> classes = new HashSet<>();

    // Bonus o malus razziali alle skill (nome skill → valore).
    // Es. Elfo: Listen +2, Search +2, Spot +2. Halfling: Climb +2, Jump +2.
    @ElementCollection
    @CollectionTable(
            name = "race_bonus_malus_skills",
            joinColumns = @JoinColumn(name = "race_id")
    )
    @MapKeyColumn(name = "skill_name")
    @Column(name = "bonus_malus")
    private Map<String, Integer> racialBonusOrMalusSkills = new HashMap<>();

    // Razza madre: null = razza base, valorizzato = sottoRaza (es. Elf → High Elf).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_race_id")
    private Race parent;

    // Lato inverso: lista delle sottoraze di questa razza base.
    // Utile per navigare da Elf a tutti i suoi tipi (High, Wood, Drow, ecc.).
    @OneToMany(mappedBy = "parent")
    private Set<Race> subRaces = new HashSet<>();
}