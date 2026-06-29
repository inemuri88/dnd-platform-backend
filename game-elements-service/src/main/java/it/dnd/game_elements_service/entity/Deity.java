package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.enumerate.Alignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta una Divinità (Deity) del pantheon di D&D 3.5.
 *
 * Le divinità sono rilevanti principalmente per:
 *   - Cleric: deve servire una divinità. La divinità determina i domini disponibili,
 *     l'allineamento consentito e l'arma sacra (favored weapon).
 *   - Paladin: segue implicitamente una divinità di allineamento LAWFUL_GOOD.
 *   - Druid: adora la natura (divinità generica "Nature") e può avere il dominio Animal o Plant.
 *
 * ALLINEAMENTO DEL CLERIC RISPETTO ALLA DIVINITÀ:
 * In D&D 3.5 il Cleric deve avere un allineamento entro un passo da quello della divinità.
 * "Un passo" significa che può differire su un solo asse (etico o morale).
 * Es. Divinità LG → Cleric può essere LG, NG, LN.
 * Il campo "allowedAlignments" memorizza esplicitamente gli allineamenti consentiti
 * per evitare di ricalcolare la regola "un passo" nel service.
 *
 * DOMINI:
 * @ManyToMany verso {@link Domain}: la divinità offre un pool di domini tra cui
 * il Cleric sceglie 2 al momento della creazione del personaggio.
 * La scelta specifica del personaggio viene gestita nel character-service.
 *
 * FAVORED WEAPON:
 * @ManyToOne verso {@link Item}: l'arma simbolo della divinità.
 * Il Cleric con il dominio War ottiene la competenza gratuita con quest'arma.
 * Nullable perché alcune divinità homebrew possono non avere arma sacra definita.
 *
 * ESEMPI (PHB/Greyhawk):
 *   Heironeous → LG, arma: Longsword, domini: Good, Law, War
 *   Pelor      → NG, arma: Heavy Mace, domini: Good, Healing, Strength, Sun
 *   Vecna      → NE, arma: Dagger, domini: Evil, Knowledge, Magic
 *   Nerull     → NE, arma: Scythe, domini: Death, Evil, Trickery
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome della divinità (es. "Heironeous", "Pelor", "Vecna")
    @Column(nullable = false, unique = true)
    private String name;

    // Titolo o epiteto (es. "The Invincible", "The Bringer of Light", "The Whispered One")
    @Column(length = 200)
    private String title;

    // Allineamento della divinità stessa
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Alignment alignment;

    // Allineamenti dei Cleric/Paladin che possono servire questa divinità.
    // Regola "un passo": massimo una differenza su un asse rispetto all'allineamento della divinità.
    // Memorizzato esplicitamente per non dover ricalcolare la regola nel service.
    @ElementCollection
    @CollectionTable(name = "deity_allowed_alignments", joinColumns = @JoinColumn(name = "deity_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "alignment")
    private Set<Alignment> allowedAlignments = new HashSet<>();

    // Pool di domini offerti da questa divinità. Il Cleric ne sceglie 2 (scelta nel character-service).
    @ManyToMany
    @JoinTable(
            name = "deity_domain",
            joinColumns = @JoinColumn(name = "deity_id"),
            inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> domains = new HashSet<>();

    // Arma sacra della divinità. Il Cleric con dominio War ottiene competenza gratuita con essa.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favored_weapon_id")
    private Item favoredWeapon;

    // Aree di influenza e portfolio divino (es. "valor, chivalry, justice" per Heironeous)
    @Column(length = 500)
    private String portfolio;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String sourceBook;
}