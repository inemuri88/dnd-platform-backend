package it.dnd.game_elements_service.entity.enumerate;

/**
 * Le 8 scuole di magia + UNIVERSAL in D&D 3.5.
 *
 * Ogni incantesimo appartiene a una sola scuola. La scuola ha rilevanza per:
 *   - La specializzazione del Wizard (uno specialist ignora una scuola e ne potenzia un'altra)
 *   - Alcune resistenze/immunità delle creature
 *   - Certi feat e class feature (es. "Spell Focus: Evocation" aumenta il DC)
 *
 * ABJURATION    → protezione, barriere, banimento (Shield, Dispel Magic)
 * CONJURATION   → evocazione di creature e sostanze, teletrasporto, cura (Summon Monster, Cure)
 * DIVINATION    → informazione e preveggenza (True Seeing, Scrying)
 * ENCHANTMENT   → controllo della mente, charme, compulsione (Charm Person, Dominate)
 * EVOCATION     → energia grezza, esplosioni, luce (Fireball, Magic Missile, Lightning Bolt)
 * ILLUSION      → inganno sensoriale, immagini, fantasmi (Invisibility, Mirror Image)
 * NECROMANCY    → morte, non morti, energia negativa/positiva (Animate Dead, Enervation)
 * TRANSMUTATION → trasformazione di materia, forma, proprietà (Polymorph, Haste, Fly)
 * UNIVERSAL     → nessuna scuola specifica; i Wizard non possono vietarla (True Strike, Wish)
 *
 * USO: Spell.school
 */
public enum SpellSchool {
    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION,
    UNIVERSAL
}