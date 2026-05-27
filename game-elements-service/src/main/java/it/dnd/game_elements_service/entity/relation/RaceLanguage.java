package it.dnd.game_elements_service.entity.relation;

import it.dnd.game_elements_service.entity.Language;
import it.dnd.game_elements_service.entity.Race;
import it.dnd.game_elements_service.entity.enumerate.RaceLanguageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity ponte tra {@link Race} e {@link Language}.
 *
 * In D&D 3.5 le lingue associate a una razza sono di due tipi:
 *   AUTOMATIC → il personaggio conosce questa lingua sin dal 1° livello,
 *               senza spendere punti (es. Common per gli Umani, Elvish per gli Elfi).
 *   BONUS     → la razza offre la possibilità di imparare questa lingua
 *               spendendo punti abilità in "Speak Language" (es. Gnoll, Goblin
 *               come bonus per gli Umani con INT alta).
 *
 * PERCHÉ NON @ManyToMany:
 * La relazione Race ↔ Language è N:M con un attributo (il tipo AUTOMATIC/BONUS).
 * Stessa motivazione di SpellLevelEntry: serve un'entity intermedia per
 * trasportare quell'attributo aggiuntivo.
 *
 * COLLOCAZIONE (cartella relation/):
 * Separata da entity/ perché non è un'entity di dominio primaria: è solo
 * una junction table con un attributo. Simile a come utilityspell/ ospita
 * le entity di supporto agli spell.
 */
@Entity
@Table(name = "race_language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RaceLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "race_id", nullable = false)
    private Race race;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    // AUTOMATIC = lingua gratuita; BONUS = disponibile ma va acquistata
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RaceLanguageType type;
}