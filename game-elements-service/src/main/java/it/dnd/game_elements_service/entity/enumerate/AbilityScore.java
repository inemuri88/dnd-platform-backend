package it.dnd.game_elements_service.entity.enumerate;

public enum AbilityScore {
    STRENGTH ("STR"),
    DEXTERITY ("DEX"),
    CONSTITUTION ("CON"),
    INTELLIGENCE ("INT"),
    WISDOM ("WIS"),
    CHARISMA ("CHA");

    private String ability;

    AbilityScore(String ability) {
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }
}
