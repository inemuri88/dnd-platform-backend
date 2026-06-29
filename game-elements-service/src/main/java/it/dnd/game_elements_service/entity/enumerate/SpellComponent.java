package it.dnd.game_elements_service.entity.enumerate;

/**
 * Componenti richieste per lanciare un incantesimo in D&D 3.5.
 *
 * V  (Verbal)       → serve pronunciare le parole (silenzio blocca lo spell)
 * S  (Somatic)      → serve un gesto con le mani (mani libere, no armatura pesante senza feat)
 * M  (Material)     → serve un oggetto fisico consumato nel lancio
 * F  (Focus)        → serve un oggetto non consumato (es. uno specchio)
 * DF (Divine Focus) → simbolo sacro/profano; sostituisce M o F per le classi divine
 * XP (XP Cost)      → costa Punti Esperienza al caster (spell potenti del PHB)
 *
 * Il testo descrittivo delle componenti M e F è memorizzato nei campi
 * materialComponents e focusComponents di {@link it.dnd.game_elements_service.entity.Spell}.
 */
public enum SpellComponent {
    V,
    S,
    M,
    F,
    DF,
    XP
}