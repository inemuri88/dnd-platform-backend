package it.dnd.game_elements_service.entity.enumerate;

/**
 * Sottocategoria di un'arma in D&D 3.5, definita su due assi:
 *   - Complessità: SIMPLE → MARTIAL → EXOTIC (crescente difficoltà d'uso)
 *   - Tipo d'uso:  MELEE (mischia) vs RANGED (distanza)
 *
 * SIMPLE:
 *   Armi base usabili da quasi tutti (dagger, quarterstaff, crossbow leggera).
 *   Tutti i personaggi non-guerrieri partono con competenza Simple.
 *
 * MARTIAL:
 *   Armi più specializzate (longsword, greatsword, longbow, shortbow).
 *   Richiedono la competenza Martial Weapons (Fighter, Paladin, Barbarian, Ranger).
 *
 * EXOTIC:
 *   Armi insolite con meccaniche particolari (bastard sword, spiked chain, shuriken).
 *   Richiedono un feat specifico per ogni arma (Exotic Weapon Proficiency: X).
 *
 * USO:
 *   Item.weaponCategory → sottocategoria dell'arma
 *   WeaponProficiency.name usa testo libero che rispecchia queste categorie
 *   (es. "Simple Weapons", "Martial Weapons", "Bastard Sword")
 */
public enum WeaponCategory {
    SIMPLE_MELEE,
    SIMPLE_RANGED,
    MARTIAL_MELEE,
    MARTIAL_RANGED,
    EXOTIC_MELEE,
    EXOTIC_RANGED
}