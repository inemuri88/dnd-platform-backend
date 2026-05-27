package it.dnd.game_elements_service.entity.utilityspell;

import it.dnd.game_elements_service.entity.ClassCharacter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Una riga della tabella "Spells Known" di una {@link ClassCharacter}.
 *
 * Questa tabella esiste SOLO per i caster spontanei (Sorcerer, Bard).
 * Un caster spontaneo non memorizza spell al mattino: conosce un numero fisso
 * di spell per livello (qui salvati) e può lanciarli liberamente finché ha slot.
 *
 * Per i caster preparati (Wizard, Cleric, Druid) questa lista è vuota:
 * conoscono tutti gli spell della propria lista (o del loro spellbook per il Wizard)
 * e la scelta avviene durante la preparazione giornaliera.
 *
 * ESEMPIO (Sorcerer, livello 5):
 *   classLevel=5 | level0=6 | level1=3 | level2=2 | level3=null | level4…9=null
 *   → il Sorcerer di 5° livello conosce 6 cantrip, 3 spell di 1°, 2 di 2°
 *
 * DIFFERENZA CON SpellsPerDayRow:
 *   SpellsPerDayRow → quanti slot ha (quante volte può lanciare)
 *   SpellsKnownRow  → quanti spell diversi conosce (il repertorio fisso)
 *
 * La lista è ordinata per classLevel grazie a @OrderBy in ClassCharacter.spellsKnown.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellsKnownRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id")
    private ClassCharacter classCharacter;

    // Livello della classe a cui si riferisce questa riga (1–20)
    private Integer classLevel;

    // Numero di spell conosciuti per livello di spell. Null = livello non ancora accessibile.
    private Integer level0Known;
    private Integer level1Known;
    private Integer level2Known;
    private Integer level3Known;
    private Integer level4Known;
    private Integer level5Known;
    private Integer level6Known;
    private Integer level7Known;
    private Integer level8Known;
    private Integer level9Known;
}