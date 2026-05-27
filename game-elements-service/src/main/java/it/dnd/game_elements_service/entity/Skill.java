package it.dnd.game_elements_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta una Skill (abilità) del sistema D&D 3.5.
 *
 * Le skill in D&D 3.5 sono capacità come Hide, Move Silently, Diplomacy, Spellcraft, ecc.
 * Ogni personaggio ha un numero di punti abilità da distribuire tra le skill disponibili.
 *
 * RELAZIONE CON ClassCharacter:
 * ClassCharacter ha un @ManyToMany verso Skill (join table "class_character_class_skill")
 * per indicare quali skill sono "di classe" per quella classe. Le skill di classe
 * costano 1 punto per rank; le cross-class costano 2 punti e hanno il massimo rank dimezzato.
 *
 * COSA NON È IN QUESTA ENTITY:
 * - La caratteristica associata (STR, DEX, INT…): quella è in principio fissa per skill,
 *   ma alcune classi/razze possono cambiare la key ability. Se serve, aggiungere un campo.
 * - Il valore del personaggio (quanti rank ha): quello sta nel character-service,
 *   non qui nel game-elements-service.
 * - Se è usabile "untrained": potrebbe essere aggiunto come flag boolean se serve.
 *
 * È intenzionalmente semplice: l'entity è un catalogo, non porta stato del personaggio.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}