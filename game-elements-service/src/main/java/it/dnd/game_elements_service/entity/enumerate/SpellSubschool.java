package it.dnd.game_elements_service.entity.enumerate;

/**
 * Sottoscuole degli incantesimi in D&D 3.5.
 *
 * Ogni sottoscuola appartiene a una scuola madre (indicata nel commento).
 * Non tutte le scuole hanno sottoscuole: Abjuration, Evocation, Necromancy
 * e Transmutation non ne hanno nel PHB base.
 *
 * La sottoscuola affina il tipo di effetto e ha rilevanza per:
 *   - Immunità specifiche (es. alcune creature sono immuni alla SUMMONING)
 *   - Interazione con certi feat o class feature
 *   - Spell Focus / Greater Spell Focus si applica alla scuola, non alla sottoscuola
 *
 * CONJURATION:
 *   CALLING      → porta una creatura da un altro piano (non una copia)
 *   CREATION     → crea materia dal nulla (cibo, acqua, oggetti)
 *   HEALING      → ripristina PF e condizioni (Cure Light Wounds, Heal)
 *   SUMMONING    → evoca una copia temporanea di una creatura
 *   TELEPORTATION→ sposta istantaneamente nello spazio (Teleport, Dimension Door)
 *
 * DIVINATION:
 *   SCRYING      → osserva a distanza tramite sensori magici (Scrying, Clairvoyance)
 *
 * ENCHANTMENT:
 *   CHARM        → modifica le emozioni verso il caster (Charm Person, Charm Monster)
 *   COMPULSION   → forza un'azione specifica (Dominate Person, Command, Hold Person)
 *
 * ILLUSION:
 *   FIGMENT      → crea immagini false senza sostanza (Silent Image, Major Image)
 *   GLAMER       → altera l'apparenza di un oggetto/persona (Disguise Self, Invisibility)
 *   PATTERN      → figura luminosa che colpisce la mente (Color Spray, Hypnotic Pattern)
 *   PHANTASM     → illusione che esiste solo nella mente del bersaglio (Phantasmal Killer)
 *   SHADOW       → usa energia dell'Plane of Shadow per creare semi-realtà (Shadow Conjuration)
 *
 * USO: Spell.subschool
 */
public enum SpellSubschool {

    // Conjuration
    CALLING,
    CREATION,
    HEALING,
    SUMMONING,
    TELEPORTATION,

    // Divination
    SCRYING,

    // Enchantment
    CHARM,
    COMPULSION,

    // Illusion
    FIGMENT,
    GLAMER,
    PATTERN,
    PHANTASM,
    SHADOW
}