package it.dnd.game_elements_service.entity.enumerate;

/**
 * Categoria di un prerequisito atomico, condivisa da {@link it.dnd.game_elements_service.entity.FeatPrerequisite}
 * e {@link it.dnd.game_elements_service.entity.ClassPrerequisite}.
 *
 * Prima questi prerequisiti memorizzavano la categoria come String libera
 * (es. "BAB", "FEAT", "SKILL"): nessuna garanzia a compile-time sui valori
 * ammessi e possibilità di typo/incoerenze nel seed data. Questo enum chiude
 * l'insieme dei valori possibili e dà type-safety al service che valida i
 * prerequisiti, mantenendo lo stesso approccio "struttura invece di testo libero".
 *
 * SIGNIFICATO DEI VALORI (abbinati a value/numericValue nelle entity prerequisito):
 *   BAB          → base attack bonus minimo            (numericValue = soglia)
 *   ABILITY      → valore minimo di una caratteristica (value = nome ability, numericValue = soglia)
 *   FEAT         → possesso di un altro feat           (value = nome feat)
 *   SKILL        → gradi minimi in una skill           (value = nome skill, numericValue = rank)
 *   CASTER_LEVEL → livello da incantatore minimo       (numericValue = soglia)
 *   ALIGNMENT    → allineamento richiesto              (value = codice Alignment)
 *   RACE         → razza richiesta                     (value = nome/codice razza)
 */
public enum PrerequisiteType {
    BAB,
    ABILITY,
    FEAT,
    SKILL,
    CASTER_LEVEL,
    ALIGNMENT,
    RACE
}