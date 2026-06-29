package it.dnd.character_service.utils;

public final class SavingThrows {
    private SavingThrows(){}

    public static int poor(int level) {
        return (int) Math.floor(0.34 * level);
    }

    public static int good(int level) {
        return (level + 4) / 2;
    }

    public static String format(int saveValue) {
        return (saveValue >= 0 ? "+" : "") + saveValue;
    }
}
