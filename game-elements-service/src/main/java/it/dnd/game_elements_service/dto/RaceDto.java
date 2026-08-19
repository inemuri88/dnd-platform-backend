package it.dnd.game_elements_service.dto;

import it.dnd.game_elements_service.entity.enumerate.AbilityScore;
import it.dnd.game_elements_service.entity.enumerate.CreatureSize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Vista "completa" di una {@link it.dnd.game_elements_service.entity.Race},
 * pensata per l'endpoint di dettaglio (GET /races/{id}).
 *
 * Questo è il DTO che dimostra il pattern "grafo aciclico": per OGNI relazione
 * bidirezionale dell'entity, qui teniamo UN SOLO verso come oggetto e
 * collassiamo il verso di ritorno a un valore piatto (id, nome o lista di nomi).
 * Risultato: un albero che Jackson e MapStruct possono percorrere senza ricorsione.
 *
 * MAPPA DEI TAGLI (come ogni ciclo dell'entity viene spezzato qui):
 *
 *  1) parent ↔ subRaces  (auto-referenziale)
 *     - verso TENUTO pieno:   subRaces  → List<RaceSummaryDto> (foglie, non RaceDto!)
 *     - verso SPEZZATO:        parent    → parentId + parentName (solo dati piatti)
 *     Così si scende verso le sottoraze ma non si risale mai alla madre.
 *
 *  2) Race.classes ↔ ClassCharacter.races  (ManyToMany bidirezionale)
 *     - verso SPEZZATO: invece di esporre le ClassCharacter complete (che a loro
 *       volta riporterebbero a Race tramite "races"), le riduciamo ai soli nomi:
 *       favoredClassNames → List<String>.
 *     NOTA DIDATTICA: in un'app reale, qui probabilmente introdurresti un
 *     ClassSummaryDto {id, name} esattamente come RaceSummaryDto. Usiamo i nomi
 *     puri solo per rispettare il vincolo "solo tre classi" di questo esempio.
 *
 * I campi @ElementCollection (mappe) NON sono relazioni verso altre entity:
 * sono valori scalari "di proprietà" della razza, quindi si copiano così come
 * sono senza alcun rischio di ciclo.
 *
 * createdAt / updatedAt arrivano dalla superclasse CreationUpdate dell'entity:
 * li includo per mostrare che anche i campi ereditati (@MappedSuperclass)
 * fluiscono normalmente nel DTO tramite i loro getter.
 */
public record RaceDto(

        Long id,
        String name,

        // Velocità base in metri e taglia: valori scalari, nessun ciclo.
        Double speed,
        CreatureSize size,

        // --- @ElementCollection: valori di proprietà della razza, copiati 1:1 ---

        // nome capacità → descrizione (es. "Darkvision" → "Vede al buio fino a 18 m")
        Map<String, String> racialSpecialSkills,
        // caratteristica → modificatore (es. DEXTERITY → +2, CONSTITUTION → -2)
        Map<AbilityScore, Integer> abilityModifiers,
        // nome skill → bonus/malus (es. "Listen" → +2)
        Map<String, Integer> racialBonusOrMalusSkills,

        // --- CICLO 1 spezzato: parent ridotto a dati piatti ---

        // id della razza madre (null se è una razza base). Niente oggetto RaceDto qui.
        Long parentId,
        // nome della razza madre, comodo per la UI senza dover fare un'altra query.
        String parentName,

        // --- CICLO 1, verso tenuto pieno: le sottorazze come FOGLIE ---

        // Le sottoraze come riepiloghi: RaceSummaryDto non ha subRaces/parent,
        // quindi la catena si ferma a un livello e non torna mai indietro.
        List<RaceSummaryDto> subRaces,

        // --- CICLO 2 spezzato: classi favorite ridotte ai soli nomi ---

        // Solo i nomi delle classi favorite: non esponiamo le ClassCharacter
        // complete, che riporterebbero a Race tramite il loro campo "races".
        List<String> favoredClassNames,

        // --- Campi ereditati da CreationUpdate ---

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}