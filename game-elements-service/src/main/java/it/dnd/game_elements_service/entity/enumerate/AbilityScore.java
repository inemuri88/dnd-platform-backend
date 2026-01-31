package it.dnd.game_elements_service.entity.enumerate;

public enum AbilityScore {
    Strength ("STR"),
    Dexterity ("DEX"),
    Constitution ("CON"),
    Intelligence ("INT"),
    Wisdom ("WIS"),
    Charisma ("CHA");

    private String ability;

    AbilityScore(String ability) {
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }
}
