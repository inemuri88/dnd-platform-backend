package it.dnd.game_elements_service.mapper;

import it.dnd.game_elements_service.dto.RaceDto;
import it.dnd.game_elements_service.dto.RaceSummaryDto;
import it.dnd.game_elements_service.entity.ClassCharacter;
import it.dnd.game_elements_service.entity.Race;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * Mapper MapStruct: converte l'entity {@link Race} nei suoi DTO.
 *
 * COS'È MAPSTRUCT (in breve):
 * È un generatore di codice. Tu dichiari solo un'interfaccia con i metodi di
 * conversione; in fase di compilazione MapStruct genera la classe di
 * implementazione (RaceMapperImpl) che fa i .getX()/.setX() al posto tuo.
 * Niente reflection a runtime: è codice Java normale, veloce e verificato dal
 * compilatore (se un campo non è mappabile, la build FALLISCE invece di
 * scoprirlo a runtime).
 *
 * ATTRIBUTO componentModel = "spring"  (spiegato per chi parte da zero):
 *
 * Punto di partenza: tu scrivi solo questa INTERFACCIA. La classe vera che fa
 * il lavoro (RaceMapperImpl) la GENERA MapStruct durante la compilazione, dentro
 * la cartella target/generated-sources/. Quindi le annotazioni finiscono su due
 * file diversi:
 *
 *   RaceMapper      (interfaccia, la scrivi tu)   → ci metti @Mapper
 *   RaceMapperImpl  (classe, la genera MapStruct) → qui serve @Component
 *
 * - @Mapper parla SOLO a MapStruct e significa "genera l'implementazione di
 *   questa interfaccia". Da solo non dice niente a Spring: senza altro, la classe
 *   generata NON è un bean e non potresti iniettarla nel service.
 *
 * - @Component da solo non basterebbe nemmeno: Spring crea bean solo da CLASSI
 *   concrete (deve poterle istanziare), non da interfacce. E la classe concreta
 *   (RaceMapperImpl) non è tua: viene rigenerata a ogni build, quindi non puoi
 *   aprirla e annotarla a mano.
 *
 * - componentModel = "spring" è il ponte tra i due mondi: dice a MapStruct
 *   "quando generi RaceMapperImpl, mettici tu sopra @Component". Risultato: la
 *   classe generata diventa un bean Spring e puoi iniettarla con la normale
 *   dependency injection, ad esempio nel costruttore di RaceService
 *   (la classe sarebbe annotata con @Service):
 *
 *       public class RaceService {
 *           private final RaceMapper raceMapper;          // l'interfaccia
 *           public RaceService(RaceMapper raceMapper) {   // Spring inietta l'Impl
 *               this.raceMapper = raceMapper;
 *           }
 *       }
 *
 * In breve: @Mapper GENERA il codice, @Component lo REGISTRA in Spring, e
 * componentModel = "spring" fa sì che MapStruct aggiunga @Component da solo
 * sulla classe generata (che tu non puoi annotare a mano).
 *
 * DOVE AVVIENE LA ROTTURA DEI CICLI:
 * Il "taglio" non è una funzione speciale: è semplicemente la FORMA dei DTO.
 * MapStruct mappa solo i campi che esistono nel target. Poiché:
 *   - RaceSummaryDto NON ha i campi subRaces/parent/classes,
 *   - RaceDto espone parent come parentId/parentName e le classi come nomi,
 * la conversione non ha alcun modo di tornare indietro e ricorrere all'infinito.
 * Qui sotto guidiamo solo i pochi casi in cui nome o tipo non coincidono 1:1.
 */
@Mapper(componentModel = "spring")
public interface RaceMapper {

    /**
     * Conversione completa Race → RaceDto (per il dettaglio di una razza).
     *
     * Le @Mapping qui sotto coprono SOLO i campi che MapStruct non saprebbe
     * mappare da solo perché nome o tipo differiscono. Tutti gli altri campi
     * con lo stesso nome (id, name, speed, size, le tre mappe @ElementCollection,
     * createdAt, updatedAt) vengono abbinati automaticamente.
     */
    // CICLO 1 spezzato: "appiattiamo" parent nei due campi piatti del DTO.
    // source = "parent.id" → MapStruct genera un accesso null-safe: se la razza
    // è una razza base (parent == null), parentId/parentName restano null.
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    // CICLO 2 spezzato: dal Set<ClassCharacter> ricaviamo solo i nomi.
    // MapStruct vede che il target è List<String> e la sorgente Set<ClassCharacter>,
    // e usa automaticamente il metodo helper toClassNames(...) qui sotto.
    @Mapping(target = "favoredClassNames", source = "classes")
    // CICLO 1, verso pieno: subRaces (Set<Race>) → List<RaceSummaryDto>.
    // Stesso nome di campo, quindi nessuna @Mapping esplicita necessaria:
    // per convertire ogni elemento Race → RaceSummaryDto MapStruct riusa
    // automaticamente il metodo toSummary(...) definito sotto. È proprio l'uso
    // della versione "summary" (foglia) che impedisce la ricorsione.
    RaceDto toDto(Race race);

    /**
     * Conversione "leggera" Race → RaceSummaryDto.
     *
     * Tutti i campi hanno lo stesso nome (id, name, size, speed): MapStruct li
     * abbina da solo, non serve nessuna @Mapping. I campi relazionali dell'entity
     * (subRaces, parent, classes, ecc.) semplicemente NON esistono nel target,
     * quindi vengono ignorati: è esattamente questo che rende il DTO una "foglia".
     */
    RaceSummaryDto toSummary(Race race);

    /**
     * Helper di supporto al ciclo 2.
     *
     * MapStruct lo seleziona in automatico per il campo favoredClassNames perché
     * la firma (Set<ClassCharacter> → List<String>) combacia con quella richiesta.
     * Lo ordiniamo per nome per avere un output stabile e prevedibile.
     * Il controllo null evita NPE quando la collezione non è inizializzata.
     */
    default List<String> toClassNames(Set<ClassCharacter> classes) {
        if (classes == null) {
            return List.of();
        }
        return classes.stream()
                .map(ClassCharacter::getName)
                .sorted()
                .toList();
    }
}