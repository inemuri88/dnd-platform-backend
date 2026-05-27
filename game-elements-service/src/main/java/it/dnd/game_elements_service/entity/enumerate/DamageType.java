package it.dnd.game_elements_service.entity.enumerate;

/**
 * Tipo di danno fisico inflitto da un'arma in D&D 3.5.
 *
 * In D&D 3.5 il tipo di danno fisico è rilevante per:
 *   - La penetrazione della Damage Reduction (DR): es. DR 5/slashing
 *     significa che 5 punti di danno non-slashing vengono ignorati
 *   - Le vulnerabilità di alcune creature (es. i non-morti subiscono
 *     effetti diversi da armi taglienti vs contundenti)
 *
 * SLASHING (taglio)       → spade, asce, daghe
 * PIERCING (perforazione) → lance, frecce, rapiere
 * BLUDGEONING (contusivo) → mazze, martelli, bastoni
 * SLASHING_PIERCING       → armi che infliggono entrambi (es. falce, guisarme)
 * SLASHING_BLUDGEONING    → armi che infliggono entrambi (es. morning star)
 *
 * Per le armi combinazione il personaggio sceglie quale tipo applicare
 * all'atto dell'attacco, in modo da aggirare la DR appropriata.
 *
 * USO: Item.damageType
 */
public enum DamageType {
    SLASHING,
    PIERCING,
    BLUDGEONING,
    SLASHING_PIERCING,
    SLASHING_BLUDGEONING
}