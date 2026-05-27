package it.dnd.game_elements_service.entity.enumerate;

/**
 * Progressione del Bonus Attacco Base (Base Attack Bonus - BAB) per livello di classe.
 *
 * In D&D 3.5 il BAB determina il numero e il bonus degli attacchi del personaggio.
 * Ogni classe ha una progressione fissa:
 *
 *   BAB_GOOD    → +1 per livello (es. Fighter, Paladin, Barbarian, Ranger)
 *                 A livello 20: +20/+15/+10/+5 (attacco iterativo ogni 5 punti)
 *
 *   BAB_AVERAGE → +¾ per livello, arrotondato per difetto (es. Cleric, Rogue, Bard)
 *                 A livello 20: +15/+10/+5
 *
 *   BAB_POOR    → +½ per livello, arrotondato per difetto (es. Wizard, Sorcerer)
 *                 A livello 20: +10/+5
 *
 * Il calcolo effettivo del BAB per livello specifico avviene nel service/character-service,
 * non qui: questa entity conserva solo il tipo di progressione.
 *
 * USO: ClassCharacter.babProgression
 */
public enum BonusBAB {
    BAB_GOOD,
    BAB_AVERAGE,
    BAB_POOR
}