package it.dnd.game_elements_service.entity.enumerate;

/**
 * Categoria di un'armatura o di uno scudo in D&D 3.5.
 *
 * La categoria determina:
 *   - Quale competenza serve per indossarla senza penalità
 *   - La penalità agli attacchi e alle skill senza competenza
 *   - Il limite al bonus DEX applicabile alla CA
 *
 * LIGHT  → Leather, Studded Leather, Chain Shirt
 *          Nessuna penalità al movimento, maxDexBonus alto
 *
 * MEDIUM → Hide, Scale Mail, Chainmail, Breastplate
 *          Riduce la velocità da 30 a 20 ft. (o da 20 a 15 ft.)
 *
 * HEAVY  → Splint Mail, Banded Mail, Half-Plate, Full Plate
 *          Riduce la velocità, armorCheckPenalty elevato
 *
 * SHIELD → Buckler, Light Shield, Heavy Shield, Tower Shield
 *          Tenuto nella mano secondaria; Tower Shield ha regole speciali
 *
 * USO:
 *   Item.armorCategory → categoria dell'oggetto armatura/scudo
 *   ArmorProficiency.name → "Light Armor", "Medium Armor", "Heavy Armor", "Shields"
 */
public enum ArmorCategory {
    LIGHT,
    MEDIUM,
    HEAVY,
    SHIELD
}