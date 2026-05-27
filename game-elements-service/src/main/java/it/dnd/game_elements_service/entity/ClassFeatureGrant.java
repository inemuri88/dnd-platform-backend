package it.dnd.game_elements_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity ponte tra {@link ClassCharacter} e {@link ClassFeature}.
 *
 * Rappresenta il fatto che "la classe X concede la feature Y al livello Z,
 * con valore numerico W". Senza questa entity intermedia la relazione
 * sarebbe una semplice @ManyToMany che non potrebbe trasportare livello e valore.
 *
 * ESEMPI DI RIGHE:
 *   classCharacter=Rogue  | feature=Sneak Attack | level=1  | numericValue=1  | description="1d6"
 *   classCharacter=Rogue  | feature=Sneak Attack | level=3  | numericValue=2  | description="2d6"
 *   classCharacter=Monk   | feature=Evasion      | level=9  | numericValue=null | description=null
 *   classCharacter=Barb   | feature=Rage         | level=1  | numericValue=1  | description="1/day"
 *
 * "numericValue": progressione scalare della feature (es. numero di dadi di Sneak Attack,
 * numero di utilizzi di Rage, bonus numerico di Lay on Hands). Null se la feature
 * non ha progressione numerica.
 *
 * "description": testo della concessione specifica a questo livello (opzionale).
 * Integra baseDescription di ClassFeature con dettagli dipendenti dal livello.
 *
 * @OrderBy("level ASC") è dichiarato in ClassCharacter.featureGrants per garantire
 * che la lista arrivi già ordinata senza dover ordinare in memoria nel service.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassFeatureGrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lato owning: questa entity conosce la sua classe
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassCharacter classCharacter;

    // La feature concessa (definizione condivisa tra più classi)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feature_id", nullable = false)
    private ClassFeature feature;

    // Livello della classe a cui la feature viene concessa (1–20)
    @Column(nullable = false)
    private int level;

    // Valore numerico della progressione (es. numero di dadi di Sneak Attack). Nullable.
    private Integer numericValue;

    // Testo descrittivo specifico di questa concessione (es. "2d6 extra damage"). Nullable.
    @Column(length = 1000)
    private String description;
}