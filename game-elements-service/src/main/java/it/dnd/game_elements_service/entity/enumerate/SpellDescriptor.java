package it.dnd.game_elements_service.entity.enumerate;

/**
 * Descrittori degli incantesimi in D&D 3.5.
 *
 * I descrittori qualificano ulteriormente l'effetto di uno spell.
 * Un incantesimo può avere più descrittori contemporaneamente.
 * Sono salvati come @ElementCollection in Spell (tabella "spell_descriptors").
 *
 * RILEVANZA MECCANICA:
 *   - Alcune creature hanno immunità o vulnerabilità legate al descrittore
 *     (es. un Fire Elemental è immune agli spell con descrittore FIRE)
 *   - Certi feat amplificano spell con descrittori specifici
 *   - Gli spell con descrittori di allineamento (CHAOTIC, EVIL, GOOD, LAWFUL)
 *     sono particolarmente efficaci contro creature dell'allineamento opposto
 *   - Gli spell MIND_AFFECTING non funzionano sui non-morti, costrutti, piante, ecc.
 *   - LANGUAGE_DEPENDENT richiede che il bersaglio capisca la lingua del caster
 *
 * TIPI ENERGETICI: ACID, COLD, ELECTRICITY, FIRE, SONIC, FORCE
 * PIANI/ELEMENTI:  AIR, EARTH, WATER
 * ALLINEAMENTI:    CHAOTIC, EVIL, GOOD, LAWFUL
 * EFFETTI MENTALI: MIND_AFFECTING, FEAR, PATTERN (quest'ultimo anche in SpellSubschool)
 * VARIE:           DARKNESS, LIGHT, DEATH, POISON, SHADOW, LANGUAGE_DEPENDENT
 *
 * USO: Spell.descriptors (Set<SpellDescriptor>)
 */
public enum SpellDescriptor {
    ACID,
    AIR,
    CHAOTIC,
    COLD,
    DARKNESS,
    DEATH,
    EARTH,
    ELECTRICITY,
    EVIL,
    FEAR,
    FIRE,
    FORCE,
    GOOD,
    LANGUAGE_DEPENDENT,
    LAWFUL,
    LIGHT,
    MIND_AFFECTING,
    POISON,
    SHADOW,
    SONIC,
    WATER
}