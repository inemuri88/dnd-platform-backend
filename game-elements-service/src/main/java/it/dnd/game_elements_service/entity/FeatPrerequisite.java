package it.dnd.game_elements_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta un singolo prerequisito di un {@link Feat}.
 *
 * DESIGN (identico a ClassPrerequisite per le prestige class):
 * Ogni riga è un requisito atomico espresso con tre campi:
 *   - type:         la categoria del requisito
 *   - value:        il target del requisito (nome feat, nome skill, enum alignment…)
 *   - numericValue: la soglia numerica dove applicabile
 *
 * Esempi di combinazioni:
 *
 *   type="BAB"          value=null             numericValue=6   → BAB +6
 *   type="ABILITY"      value="STRENGTH"       numericValue=13  → Forza 13
 *   type="FEAT"         value="Dodge"          numericValue=null→ Possedere Dodge
 *   type="SKILL"        value="Tumble"         numericValue=4   → 4 gradi in Tumble
 *   type="CASTER_LEVEL" value=null             numericValue=3   → Caster level 3
 *   type="ALIGNMENT"    value="LAWFUL_GOOD"    numericValue=null→ Allineamento LG
 *   type="RACE"         value="ELF"            numericValue=null→ Solo razze elfo
 *
 * Questo approccio preferisce la struttura al testo libero: consente al service
 * layer di validare automaticamente se un personaggio soddisfa i requisiti,
 * senza parsing di stringhe narrative.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatPrerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione owning-side: FeatPrerequisite conosce il suo Feat.
    // La cascade è dichiarata sul lato Feat (@OneToMany), non qui.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feat_id", nullable = false)
    private Feat feat;

    // Categoria del prerequisito: "BAB", "FEAT", "SKILL", "ABILITY",
    // "CASTER_LEVEL", "ALIGNMENT", "RACE"
    private String type;

    // Valore qualitativo: nome del feat richiesto, nome dell'abilità, ecc.
    // Null per i requisiti puramente numerici (es. BAB).
    private String value;

    // Soglia numerica: gradi di skill, valore di caratteristica, BAB minimo, ecc.
    // Null per i requisiti qualitativi puri (es. possedere un feat).
    private Integer numericValue;
}