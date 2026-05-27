package it.dnd.game_elements_service.entity.proficiency;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Rappresenta un tipo di competenza con le armature (Armor Proficiency).
 *
 * In D&D 3.5 la competenza con le armature si divide in categorie:
 *   - "Light Armor"  → tutte le armature leggere
 *   - "Medium Armor" → tutte le armature medie (richiede Light)
 *   - "Heavy Armor"  → tutte le armature pesanti (richiede Medium)
 *   - "Shields"      → tutti gli scudi normali
 *   - "Tower Shield" → scudo torre (ha le sue regole speciali)
 *
 * Senza la competenza appropriata il personaggio subisce una penalità agli
 * attacchi e alle skill fisiche pari all'armorCheckPenalty dell'armatura.
 *
 * RELAZIONE:
 * ClassCharacter ha un @ManyToMany verso ArmorProficiency.
 * La join table si chiama "class_character_armor_proficiency".
 * È condivisa tra più classi (es. Fighter, Paladin, Cleric hanno tutte
 * Heavy Armor Proficiency) → @ManyToMany è la scelta corretta.
 *
 * NON ha Lombok per lo stesso motivo di ClassPrerequisite: entity semplice
 * senza logica propria. Aggiungere se serve accesso da fuori del package.
 */
@Entity
public class ArmorProficiency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Es. "Light Armor", "Medium Armor", "Heavy Armor", "Shields", "Tower Shield"
    private String name;

    private String description;
}