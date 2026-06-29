package it.dnd.game_elements_service.entity.utilityspell;

import it.dnd.game_elements_service.entity.ClassCharacter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Una riga della tabella "Spells Per Day" di una {@link ClassCharacter}.
 *
 * In D&D 3.5 ogni classe che lancia incantesimi ha una tabella di progressione
 * che indica quanti slot incantesimo ha disponibili per ogni livello classe.
 * Questa entity rappresenta una singola riga di quella tabella.
 *
 * ESEMPIO (Wizard, livello 5):
 *   classLevel=5 | level0=4 | level1=4 | level2=3 | level3=2 | level4=1 | level5…9=null
 *
 * CONVENZIONE:
 * - null significa "non ancora accessibile a questo livello classe"
 * - 0 significa "slot disponibili solo per spell bonus da caratteristica alta" (raro)
 * - Per le classi non-caster questa tabella è vuota (ClassCharacter.spellcaster=false)
 *
 * BONUS SPELL DA CARATTERISTICA:
 * Gli slot bonus per caratteristica alta (es. Wizard con INT 18 guadagna slot extra)
 * si calcolano nel service, non sono memorizzati qui. Questa tabella contiene
 * solo i valori base da manuale.
 *
 * La lista è ordinata per classLevel grazie a @OrderBy in ClassCharacter.spellsPerDay.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellsPerDayRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id")
    private ClassCharacter classCharacter;

    // Livello della classe a cui si riferisce questa riga (1–20)
    private Integer classLevel;

    // Slot per livello di spell (0 = cantrip, 9 = massimo). Null = non ancora accessibile.
    private Integer level0Spells;
    private Integer level1Spells;
    private Integer level2Spells;
    private Integer level3Spells;
    private Integer level4Spells;
    private Integer level5Spells;
    private Integer level6Spells;
    private Integer level7Spells;
    private Integer level8Spells;
    private Integer level9Spells;
}