package it.dnd.game_elements_service.entity.enumerate;

import lombok.Getter;

/**
 * Dado vita (Hit Die) di una {@link it.dnd.game_elements_service.entity.ClassCharacter}.
 *
 * Ogni livello di classe il personaggio tira il proprio dado vita e aggiunge
 * il modificatore di Costituzione al risultato per ottenere i PF guadagnati.
 * Al 1° livello si prende il massimo del dado (senza tirarlo).
 *
 * CLASSI DI RIFERIMENTO:
 *   D4  → Wizard, Sorcerer (caster puri, fragili in combattimento)
 *   D6  → Rogue, Bard
 *   D8  → Cleric, Druid, Monk, Ranger (la grande maggioranza delle classi)
 *   D10 → Fighter, Paladin (guerrieri corazzati)
 *   D12 → Barbarian (la classe più resistente fisicamente)
 *
 * Il campo "die" memorizza il valore numerico per eventuali calcoli nel service
 * (es. calcolo PF medi: die/2 + 1 per livello).
 *
 * USO: ClassCharacter.hitDie
 */
@Getter
public enum HitDie {
    D4 (4),
    D6 (6),
    D8 (8),
    D10(10),
    D12(12);

    private final int die;

    HitDie(int die) {
        this.die = die;
    }
}