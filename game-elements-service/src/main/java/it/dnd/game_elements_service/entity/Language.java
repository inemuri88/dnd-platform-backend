package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Language extends CreationUpdate {
//TODO: linguaggi bonus rispetto alla classe oltre che alla razza(che è stata già fatta)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String alphabet; //speak or script

    @Column(nullable = false)
    private boolean secret;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return id != null && Objects.equals(id,language.id) && Objects.equals(name, language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
