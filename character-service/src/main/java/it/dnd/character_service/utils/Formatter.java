package it.dnd.character_service.utils;

@FunctionalInterface
public interface Formatter<T> {
    String format(T value);
}