package it.dnd.game_elements_service.entity.utilityspell;

import it.dnd.game_elements_service.entity.Spell;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity ponte tra {@link Spell} e {@link SpellList}.
 *
 * PERCHÉ NON @ManyToMany:
 * La relazione Spell ↔ SpellList è N:M ma porta un attributo (il livello
 * dell'incantesimo in quella lista). Una @ManyToMany pura non può esprimere
 * questo attributo extra, quindi si usa un'entity intermedia esplicita.
 *
 * Esempio di dati prodotti:
 *   spell_id=42 (Fireball) | spell_list_id=1 (Wizard List) | spellLevel=3
 *   spell_id=42 (Fireball) | spell_list_id=2 (Sorcerer List) | spellLevel=3
 *   spell_id=15 (Cure Light Wounds) | spell_list_id=3 (Cleric List) | spellLevel=1
 *
 * COLLOCAZIONE (cartella utilityspell/):
 * Stessa cartella di SpellsPerDayRow e SpellsKnownRow, che seguono lo stesso
 * pattern: entity "di servizio" che supportano ClassCharacter e Spell senza
 * essere entity di dominio primarie.
 *
 * La cascade è dichiarata sul lato Spell (@OneToMany), non qui.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellLevelEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spell_id", nullable = false)
    private Spell spell;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spell_list_id", nullable = false)
    private SpellList spellList;

    // Livello dell'incantesimo in questa lista specifica (0 = cantrip, max 9)
    @Column(nullable = false)
    private Integer spellLevel;
}