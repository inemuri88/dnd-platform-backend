package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import it.dnd.game_elements_service.entity.enumerate.AbilityScore;
import it.dnd.game_elements_service.entity.enumerate.CreatureSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Race extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double speed;

    @Column(nullable = false)
    private CreatureSize size;

    /*
    TODO:
        Se nella descrizione dell'abilità c'è una distanza per l'effetto
        allora verrà parsata nel service con un metodo apposito
     */
    //crea una tabella con il nome della special skill e con la sua descrizione
    @ElementCollection
    @CollectionTable(
            name = "race_racial_skills",
            joinColumns = @JoinColumn(name = "race_id")
    )
    @MapKeyColumn(name = "skill_name")
    @Column(name = "description")
    private Map<String, String> racialSkills = new HashMap<>();

    //come sopra, ma qui ho i modificatori razziali
    @ElementCollection
    @CollectionTable(
            name = "race_ability_mod",
            joinColumns = @JoinColumn(name = "race_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "ability_score")
    @Column(name = "modifier", nullable = false)
    private Map<AbilityScore, Integer> abilityModifiers = new EnumMap<>(AbilityScore.class);

//TODO: aggiungere: classe preferita, bonus razziali su abilità
}
