package it.dnd.game_elements_service.dto;


import it.dnd.game_elements_service.entity.enumerate.AbilityScore;
import it.dnd.game_elements_service.entity.enumerate.CreatureSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;
import java.util.Set;

/**
 * DTO di input per creare/aggiornare una Race.
 * Le relazioni verso altre entity sono referenziate per ID (non con l'entity intera):
 * il service risolve gli id in entity gestite prima di salvare.
 */
public record RaceSaveDto(

        @NotBlank
        String name,

        @NotNull @Positive
        Double speed,

        @NotNull
        CreatureSize size,

        // @ElementCollection: valori di proprietà, si copiano 1:1 (nessun ciclo)
        Map<String, String> racialSpecialSkills,
        Map<AbilityScore, Integer> abilityModifiers,
        Map<String, Integer> racialBonusOrMalusSkills,

        // classi favorite referenziate per id (non l'entity ClassCharacter)
        Set<Long> favoredClassIds,

        // razza madre per id (null = razza base). Niente entity Race ciclica.
        Long parentId
) {
}
