package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * Rappresenta una lingua parlata (e/o scritta) nel mondo di D&D 3.5.
 *
 * In D&D 3.5 ogni personaggio conosce automaticamente alcune lingue in base alla
 * propria razza, e può apprenderne altre spendendo punti abilità in "Speak Language".
 * La relazione con le razze è gestita da {@link it.dnd.game_elements_service.entity.relation.RaceLanguage}.
 *
 * CAMPO "secret":
 * Alcune lingue non sono disponibili liberamente: Thieves' Cant (Argot dei ladri)
 * e Druidic si apprendono solo entrando in specifiche classi/organizzazioni,
 * non tramite punti skill. Il flag secret=true segnala questo vincolo al service.
 *
 * CAMPO "alphabet":
 * In D&D 3.5 le lingue usano alfabeti condivisi: Common e molte lingue umane usano
 * l'alfabeto Common; Elvish usa Elvish; Dwarven usa Dwarven; Draconic è usato
 * da Draconic, Kobold e Orc. Questo campo vale "speak" per lingue solo orali
 * (come Druidic che non ha scrittura standard) o il nome dell'alfabeto.
 *
 * TODO: aggiungere supporto per i linguaggi bonus concessi dalla classe
 * (es. Wizard sceglie lingue bonus in base a INT), oltre ai bonus razziali
 * già gestiti in RaceLanguage con tipo BONUS.
 *
 * equals/hashCode basati su id + name: permette di usare Language in Set
 * senza duplicati e confrontarli prima che Hibernate li persista (id=null).
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Language extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Nome dell'alfabeto usato per scrivere questa lingua, oppure "speak" se solo orale.
    // Es. "Common", "Elvish", "Dwarven", "Draconic"
    private String alphabet;

    // true = lingua segreta, non acquistabile tramite skill point (es. Druidic, Thieves' Cant)
    @Column(nullable = false)
    private boolean secret;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id != null && Objects.equals(id, language.id) && Objects.equals(name, language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}