package it.dnd.game_elements_service.entity.utilityspell;

import it.dnd.game_elements_service.entity.Domain;
import it.dnd.game_elements_service.entity.Spell;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity ponte tra {@link Domain} e {@link Spell}.
 *
 * Ogni dominio ha esattamente uno spell per livello (1–9). Questa entity
 * rappresenta quella relazione portando l'attributo "spellLevel".
 *
 * PERCHÉ NON @ManyToMany:
 * Stessa motivazione di {@link SpellLevelEntry}: la relazione Domain ↔ Spell
 * porta un attributo (il livello nel dominio), che @ManyToMany non può esprimere.
 *
 * ESEMPI DI RIGHE (dominio Fire):
 *   domain=Fire | spell=Burning Hands   | spellLevel=1
 *   domain=Fire | spell=Produce Flame   | spellLevel=2
 *   domain=Fire | spell=Resist Energy   | spellLevel=3
 *   domain=Fire | spell=Wall of Fire    | spellLevel=4
 *   domain=Fire | spell=Fire Shield     | spellLevel=5
 *   domain=Fire | spell=Fire Seeds      | spellLevel=6
 *   domain=Fire | spell=Fire Storm      | spellLevel=7
 *   domain=Fire | spell=Incendiary Cloud| spellLevel=8
 *   domain=Fire | spell=Elemental Swarm | spellLevel=9
 *
 * NOTA: lo stesso spell può comparire in più domini a livelli diversi.
 * Es. "Resist Energy" è dominio Fire 3 e dominio Protection 3.
 *
 * La cascade è dichiarata sul lato Domain (@OneToMany), non qui.
 * La lista è ordinata per spellLevel grazie a @OrderBy in Domain.domainSpells.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainSpellEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spell_id", nullable = false)
    private Spell spell;

    // Livello dello spell in questo dominio (1–9)
    @Column(nullable = false)
    private Integer spellLevel;
}