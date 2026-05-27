package it.dnd.game_elements_service.entity.enumerate;

/**
 * Categoria principale di un oggetto ({@link it.dnd.game_elements_service.entity.Item}).
 *
 * La categoria determina quali campi di Item sono rilevanti e le regole
 * applicabili all'oggetto:
 *
 *   WEAPON    → weaponCategory, damageType, damageMedium, critical, rangeIncrement
 *               Richiede WeaponProficiency per usarla senza penalità
 *
 *   ARMOR     → armorCategory, armorBonus, maxDexBonus, armorCheckPenalty, arcaneSpellFailure
 *               Richiede ArmorProficiency per indossarla senza penalità
 *
 *   SHIELD    → come ARMOR, ma armorCategory=SHIELD; occupa la mano secondaria
 *
 *   CONSUMABLE→ oggetti a uso singolo (pozioni, pergamene, bombe d'acido)
 *               Nessun campo statistico specifico; le regole sono in description
 *
 *   WONDROUS  → oggetti magici permanenti (anelli, mantelli, borse extradimensionali)
 *               Le regole sono in description; possono avere un costo alto in costGp
 *
 *   TOOL      → strumenti per skill (kit da ladro, kit da erbalista, strumenti musicali)
 *               Possono dare bonus alle skill; dettagli in description
 */
public enum ItemCategory {
    WEAPON,
    ARMOR,
    SHIELD,
    CONSUMABLE,
    WONDROUS,
    TOOL
}