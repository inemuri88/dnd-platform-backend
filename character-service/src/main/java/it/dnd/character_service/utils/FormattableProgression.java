package it.dnd.character_service.utils;

public record FormattableProgression<T>(Progression<T> progression, Formatter<T> formatter) {

    public T atLevel(int level) {
        return progression.atLevel(level);
    }

    public String formattedAtLevel(int level) {
        return formatter.format(progression.atLevel(level));
    }
}