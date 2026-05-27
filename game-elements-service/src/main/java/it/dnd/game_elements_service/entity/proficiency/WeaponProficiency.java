package it.dnd.game_elements_service.entity.proficiency;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Rappresenta un tipo di competenza con le armi (Weapon Proficiency).
 *
 * In D&D 3.5 la competenza con le armi può essere:
 *   - Categoriale: "Simple Weapons", "Martial Weapons", "Exotic Weapons"
 *   - Singola: "Bastard Sword", "Hand Crossbow" (per le armi esotiche)
 *
 * Senza competenza, il personaggio subisce una penalità di -4 agli attacchi.
 *
 * GRANULARITÀ:
 * Si possono creare entry sia categoriali che singole:
 *   name="Simple Weapons"  → tutti i caster, Fighter, Rogue, ecc.
 *   name="Martial Weapons" → Fighter, Paladin, Ranger, Barbarian
 *   name="Bastard Sword"   → chi ha preso l'Exotic Weapon Proficiency specifica
 *
 * RELAZIONE:
 * ClassCharacter ha un @ManyToMany verso WeaponProficiency.
 * La join table si chiama "class_character_weapon_proficiency".
 *
 * Stessa struttura di ArmorProficiency, nella stessa cartella proficiency/
 * per raggruppamento logico.
 */
@Entity
public class WeaponProficiency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Es. "Simple Weapons", "Martial Weapons", "Exotic Weapons", "Bastard Sword"
    private String name;

    private String description;
}