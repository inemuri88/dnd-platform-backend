package it.dnd.character_service.utils;

import it.dnd.character_service.entity.enumerate.BonusType;

public final class Progressions {
    private Progressions(){}


    public static final FormattableProgression<int[]> BAB_GOOD =
            new FormattableProgression<>(BAB::good, BAB::format);

    public static final FormattableProgression<int[]> BAB_AVERAGE =
            new FormattableProgression<>(BAB::average, BAB::format);

    public static final FormattableProgression<int[]> BAB_POOR =
            new FormattableProgression<>(BAB::poor, BAB::format);

    public static final FormattableProgression<Integer> SAVE_GOOD =
            new FormattableProgression<>(SavingThrows::good, SavingThrows::format);

    public static final FormattableProgression<Integer> SAVE_POOR =
            new FormattableProgression<>(SavingThrows::poor, SavingThrows::format);

    public static FormattableProgression<?> byType(BonusType type) {
        return switch (type) {
            case BAB_GOOD -> BAB_GOOD;
            case BAB_AVERAGE -> BAB_AVERAGE;
            case BAB_POOR -> BAB_POOR;
            case SAVE_GOOD -> SAVE_GOOD;
            case SAVE_POOR -> SAVE_POOR;
        };
    }

    public static String format(BonusType type, int level) {
        return byType(type).formattedAtLevel(level);
    }
}