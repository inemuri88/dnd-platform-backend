package it.dnd.character_service.utils;

@FunctionalInterface
public interface Progression<T> {
    T atLevel(int level);
}
