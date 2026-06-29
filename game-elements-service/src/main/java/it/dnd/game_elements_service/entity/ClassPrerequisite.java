package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.enumerate.PrerequisiteType;
import jakarta.persistence.*;

/**
 * Prerequisito di accesso a una {@link ClassCharacter}, usato principalmente
 * per le Prestige Class (classi di prestigio) del sistema D&D 3.5.
 *
 * Le prestige class hanno requisiti stringenti per l'accesso: BAB minimo,
 * rank nelle skill, possesso di feat specifici, allineamento, ecc.
 *
 * STRUTTURA DEL DATO (identica a FeatPrerequisite):
 *   type + value + numericValue → un singolo requisito atomico.
 *
 * Esempi di combinazioni:
 *   type="BAB"          value=null                numericValue=5  → BAB +5
 *   type="SKILL"        value="Knowledge(arcana)" numericValue=8  → 8 ranks
 *   type="FEAT"         value="Combat Casting"    numericValue=null → possedere il feat
 *   type="ALIGNMENT"    value="LAWFUL_GOOD"       numericValue=null → solo LG
 *   type="CASTER_LEVEL" value=null                numericValue=1  → almeno 1 livello di caster
 *
 * NON ha Lombok: entity semplice con soli campi primitivi/String, il cui accesso
 * è sempre mediato via ClassCharacter (cascade ALL). Se in futuro serve un DTO
 * o un service, aggiungere @Getter/@Setter/Lombok è immediato.
 */
@Entity
public class ClassPrerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lato owning: conosce la classe di cui è prerequisito
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id")
    private ClassCharacter classCharacter;

    // Categoria del requisito (BAB, SKILL, FEAT, ALIGNMENT, CASTER_LEVEL, RACE).
    // FIX: prima era una String libera (nessun controllo sui valori ammessi, rischio
    // di typo nel seed data). Ora è l'enum PrerequisiteType, mappato come STRING per
    // leggibilità e robustezza al riordino dei valori.
    @Enumerated(EnumType.STRING)
    private PrerequisiteType type;

    // Target qualitativo: nome feat, nome skill, codice allineamento, ecc. Null se numerico puro.
    private String value;

    // Soglia numerica: rank richiesti, BAB minimo, caster level, ecc. Null se qualitativo puro.
    private Integer numericValue;
}