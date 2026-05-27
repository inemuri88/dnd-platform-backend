package it.dnd.game_elements_service.entity.enumerate;

/**
 * Categorie di taglia delle creature in D&D 3.5.
 *
 * La taglia influenza numerosi aspetti meccanici:
 *   - Modificatore alla CA e all'attacco (FINE +8 → COLOSSAL -8)
 *   - Spazio occupato e portata naturale
 *   - Modificatore ai tiri di Grapple
 *   - Danno delle armi naturali (un gigante colpisce per danni maggiori)
 *   - Capacità di carico (LARGE porta il doppio di MEDIUM)
 *   - Modificatori alle skill fisiche (Climb, Jump, Hide, Move Silently)
 *
 * ORDINE CRESCENTE DI TAGLIA:
 *   FINE (pochi cm) → DIMINUTIVE → TINY → SMALL → MEDIUM → LARGE → HUGE → GARGANTUAN → COLOSSAL
 *
 * I personaggi giocanti standard (Umano, Elfo, Nano, Gnomo, Halfling ecc.) sono
 * SMALL o MEDIUM. Le razze mostruose possono essere LARGE.
 *
 * USO: Race.size
 */
public enum CreatureSize {
    FINE,
    DIMINUTIVE,
    TINY,
    SMALL,
    MEDIUM,
    LARGE,
    HUGE,
    GARGANTUAN,
    COLOSSAL
}