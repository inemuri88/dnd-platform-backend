package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.enumerate.FeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un Talento (Feat) del sistema D&D 3.5.
 *
 * I feat sono capacità speciali che i personaggi acquisiscono ogni 3 livelli
 * (1°, 3°, 6°, 9° ecc.) o come bonus dalla propria classe (es. Fighter).
 * Ogni feat ha dei prerequisiti (abilità minima, altri feat, BAB, ecc.)
 * e un beneficio meccanico ben definito dal manuale.
 *
 * STRUTTURA DATI:
 * - I prerequisiti sono separati in {@link FeatPrerequisite}: ogni riga è un
 *   singolo requisito (es. "ABILITY STRENGTH 13" oppure "FEAT Combat Expertise").
 *   Questo permette di filtrare o validare i prerequisiti programmaticamente
 *   senza dover parsare testo libero.
 *
 * PATTERN SEGUITO (uguale a ClassCharacter / ClassPrerequisite):
 * - @OneToMany con cascade ALL + orphanRemoval: i prerequisiti appartengono
 *   esclusivamente a questo feat; se il feat viene eliminato, vengono rimossi
 *   automaticamente anche i suoi prerequisiti.
 * - Nessuna estensione di CreationUpdate: i feat sono dati statici di
 *   sistema (seed data), non entità che l'utente modifica nel tempo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome ufficiale del feat come appare nel manuale (es. "Power Attack")
    @Column(nullable = false, unique = true)
    private String name;

    // Categoria del feat (vedi FeatType). Determina chi può prenderlo:
    // FIGHTER = solo il fighter come bonus feat, METAMAGIC = modifica gli spell, ecc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeatType type;

    // Testo della sezione "Benefit" del PHB: descrive il vantaggio meccanico
    @Column(length = 3000)
    private String benefit;

    // Testo della sezione "Normal" del PHB: cosa puoi fare SENZA questo feat.
    // Non tutti i feat hanno questa sezione (nullable di default).
    @Column(length = 1000)
    private String normal;

    // Testo della sezione "Special" del PHB: note aggiuntive (es. interazioni
    // con altre classi, possibilità di prendere il feat più volte, ecc.)
    @Column(length = 1000)
    private String special;

    // Libro sorgente (es. "PHB", "Complete Warrior", "Expanded Psionics")
    @Column(length = 200)
    private String sourceBook;

    // Lista dei prerequisiti strutturati. Ogni elemento è una singola condizione
    // (es. BAB >= 5, oppure possedere "Dodge"). Vedi FeatPrerequisite per i dettagli.
    @OneToMany(mappedBy = "feat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatPrerequisite> prerequisites = new ArrayList<>();
}