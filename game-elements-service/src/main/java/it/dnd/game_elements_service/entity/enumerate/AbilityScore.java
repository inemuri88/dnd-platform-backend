package it.dnd.game_elements_service.entity.enumerate;

import lombok.Getter;

/**
 * Le 6 caratteristiche (Ability Score) fondamentali di D&D 3.5.
 *
 * Ogni caratteristica ha un'abbreviazione standard a 3 lettere usata
 * universalmente nei manuali e nelle schede del personaggio.
 *
 * USI NEL MODELLO:
 *   Race.abilityModifiers         → Map<AbilityScore, Integer>: bonus/malus razziali
 *   ClassCharacter.skillKeyAbility → caratteristica che potenzia i punti skill (quasi sempre INT)
 *   ClassCharacter.castingAbility  → caratteristica che determina DC e slot bonus per gli spell
 *
 * ESEMPI DI UTILIZZO (regole D&D 3.5):
 *   STRENGTH     → attacchi in mischia, danni mischia, Climb, Jump, Swim
 *   DEXTERITY    → CA, attacchi a distanza, Reflex save, Hide, Move Silently
 *   CONSTITUTION → PF extra per livello, Fortitude save, Concentration
 *   INTELLIGENCE → punti skill per livello, skill Knowledges, casting Wizard
 *   WISDOM       → Perception (Spot, Listen, Sense Motive), Will save, casting Cleric/Druid
 *   CHARISMA     → Turn Undead, Diplomacy, Bluff, casting Sorcerer/Bard
 */
@Getter
public enum AbilityScore {
    STRENGTH     ("STR"),
    DEXTERITY    ("DEX"),
    CONSTITUTION ("CON"),
    INTELLIGENCE ("INT"),
    WISDOM       ("WIS"),
    CHARISMA     ("CHA");

    private final String ability;

    AbilityScore(String ability) {
        this.ability = ability;
    }
}