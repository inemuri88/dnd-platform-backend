package it.dnd.game_elements_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta la definizione di una Class Feature in D&D 3.5.
 *
 * Una class feature è una capacità speciale concessa da una classe a un certo livello:
 * es. "Sneak Attack", "Rage", "Turn Undead", "Evasion", "Spellcasting".
 *
 * SEPARAZIONE DALLA CONCESSIONE:
 * Questa entity contiene solo la definizione (nome + descrizione base).
 * La concessione concreta — quale classe la ottiene, a quale livello, con quale
 * valore numerico — è in {@link ClassFeatureGrant}.
 *
 * Questo design permette a più classi di condividere la stessa feature:
 * es. "Evasion" è concessa sia al Rogue che al Monk (a livelli diversi),
 * ma la descrizione è identica e non va duplicata.
 *
 * "baseDescription": la descrizione generale della feature. Se la feature
 * ha varianti per livello (es. "Sneak Attack +1d6" al 1°, "+2d6" al 3°),
 * il testo scalabile va in ClassFeatureGrant.description, non qui.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome univoco della feature (es. "Sneak Attack", "Rage", "Turn Undead")
    @Column(nullable = false, unique = true)
    private String name;

    // Descrizione base del funzionamento meccanico della feature
    @Column(length = 2000)
    private String baseDescription;
}