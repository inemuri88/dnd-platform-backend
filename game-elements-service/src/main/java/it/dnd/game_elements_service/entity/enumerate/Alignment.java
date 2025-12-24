package it.dnd.game_elements_service.entity.enumerate;

public enum Alignment {

    LAWFUL_GOOD("LG"),
    NEUTRAL_GOOD("NG"),
    CHAOTIC_GOOD("CG"),

    LAWFUL_NEUTRAL("LN"),
    TRUE_NEUTRAL("N"),
    CHAOTIC_NEUTRAL("CN"),

    LAWFUL_EVIL("LE"),
    NEUTRAL_EVIL("NE"),
    CHAOTIC_EVIL("CE");

    private final String code;

    Alignment(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
