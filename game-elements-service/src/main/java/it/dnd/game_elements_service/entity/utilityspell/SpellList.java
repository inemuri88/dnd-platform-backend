package it.dnd.game_elements_service.entity.utilityspell;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lista degli incantesimi associata a una classe che lancia magie.
 *
 * In D&D 3.5 ogni classe arcana o divina ha la propria lista di incantesimi
 * (es. "Wizard Spell List", "Cleric Spell List", "Druid Spell List").
 * Classi diverse possono condividere la stessa lista (es. Wizard e Sorcerer
 * usano entrambe la lista degli Arcane spell, ma con progressioni diverse).
 *
 * RELAZIONI:
 *   ClassCharacter → SpellList  (@ManyToOne): ogni classe punta alla propria lista.
 *   Spell → SpellList           (via SpellLevelEntry): ogni spell dichiara
 *                                a quale lista appartiene e a che livello.
 *
 * Il campo "code" è la chiave tecnica stabile usata nel seed data e nelle API
 * (es. "WIZARD_LIST"); "name" è il label leggibile (es. "Wizard Spell List").
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Chiave tecnica stabile, non aggiornabile dopo la creazione
    @Column(nullable = false, unique = true, updatable = false)
    private String code; // es. "WIZARD_LIST", "CLERIC_LIST"

    @Column(nullable = false)
    private String name; // es. "Wizard Spell List"
}