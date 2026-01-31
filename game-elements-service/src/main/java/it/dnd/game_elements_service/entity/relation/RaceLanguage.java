package it.dnd.game_elements_service.entity.relation;

import it.dnd.game_elements_service.entity.Language;
import it.dnd.game_elements_service.entity.Race;
import it.dnd.game_elements_service.entity.enumerate.RaceLanguageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RaceLanguageType type;
}
