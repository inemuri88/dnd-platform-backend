package it.dnd.game_elements_service.entity.enumerate;

/**
 * I 9 allineamenti del sistema D&D 3.5.
 *
 * L'allineamento è definito da due assi:
 *   Asse etico:  LAWFUL → NEUTRAL → CHAOTIC
 *   Asse morale: GOOD   → NEUTRAL → EVIL
 *
 * La combinazione dei due assi produce la griglia 3×3:
 *   LG  NG  CG
 *   LN  N   CN
 *   LE  NE  CE
 *
 * USO NEL MODELLO:
 *   ClassCharacter.allowedAlignments → gli allineamenti ammessi per quella classe.
 *   Es. Paladin = solo {LAWFUL_GOOD}; Monk = tutti i LAWFUL_*; Barbarian = tutti eccetto LAWFUL_*.
 *   FeatPrerequisite.value → prerequisito di allineamento per certi feat (es. "LAWFUL_GOOD").
 *
 * Il campo "code" usa l'abbreviazione ufficiale (es. "LG", "N" per True Neutral).
 */
public enum Alignment {

    LAWFUL_GOOD     ("LG"),
    NEUTRAL_GOOD    ("NG"),
    CHAOTIC_GOOD    ("CG"),

    LAWFUL_NEUTRAL  ("LN"),
    TRUE_NEUTRAL    ("N"),
    CHAOTIC_NEUTRAL ("CN"),

    LAWFUL_EVIL     ("LE"),
    NEUTRAL_EVIL    ("NE"),
    CHAOTIC_EVIL    ("CE");

    private final String code;

    Alignment(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}