package it.dnd.game_elements_service.service;

import it.dnd.game_elements_service.dto.RaceDto;
import it.dnd.game_elements_service.dto.RaceSummaryDto;
import it.dnd.game_elements_service.entity.Race;
import it.dnd.game_elements_service.mapper.RaceMapper;
import it.dnd.game_elements_service.repository.RaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service per le razze: contiene la logica di business e fa da "ponte" tra il
 * repository (entity dal DB) e il mondo esterno (DTO verso il controller).
 *
 * IL FLUSSO COMPLETO che mette insieme i pezzi visti finora:
 *
 *     Controller → Service → Repository → DB
 *                     │
 *                     └── Mapper (Entity → DTO)
 *
 * Il service:
 *   1. chiede le entity al RaceRepository,
 *   2. le converte in DTO col RaceMapper,
 *   3. restituisce i DTO (mai le entity!) verso l'esterno.
 *
 * PERCHÉ RESTITUIRE DTO E NON ENTITY:
 * È qui che il lavoro fatto sui DTO "aciclici" diventa concreto. Restituendo
 * RaceDto/RaceSummaryDto (e non Race), evitiamo la ricorsione infinita in
 * serializzazione JSON e non esponiamo all'esterno la struttura interna del DB.
 *
 * LE DUE DIPENDENZE (iniettate da Spring nel costruttore):
 *   - RaceRepository → per leggere/scrivere le razze
 *   - RaceMapper     → per trasformarle in DTO
 * Usiamo la "constructor injection": Spring vede il costruttore e passa da solo
 * le due implementazioni (RaceRepositoryImpl e RaceMapperImpl, entrambe bean
 * generati). È il motivo per cui sul mapper serviva componentModel = "spring":
 * senza, il RaceMapper non sarebbe un bean e questa iniezione fallirebbe.
 */
@Service
public class RaceService {

    private final RaceRepository raceRepository;
    private final RaceMapper raceMapper;

    // Constructor injection: niente @Autowired necessario quando c'è un solo
    // costruttore. Spring inietta automaticamente i due bean. I campi sono final
    // perché, una volta costruito il service, non devono più cambiare.
    public RaceService(RaceRepository raceRepository, RaceMapper raceMapper) {
        this.raceRepository = raceRepository;
        this.raceMapper = raceMapper;
    }

    /**
     * Restituisce il DETTAGLIO completo di una razza (vista RaceDto).
     *
     * @Transactional(readOnly = true) È IL PUNTO CRUCIALE QUI:
     * la mappatura verso RaceDto accede a campi LAZY dell'entity (subRaces,
     * classes, parent), che Hibernate carica solo quando vengono usati. Se la
     * conversione avvenisse FUORI da una transazione, la sessione sarebbe già
     * chiusa e otterresti la famigerata LazyInitializationException.
     * Tenendo il mapping DENTRO il metodo @Transactional, la sessione resta
     * aperta finché il mapper non ha finito di leggere tutto ciò che gli serve.
     * "readOnly = true" è un'ottimizzazione: dichiara che non scriveremo nulla.
     *
     * @throws EntityNotFoundException se non esiste una razza con quell'id.
     */
    @Transactional(readOnly = true)
    public RaceDto getById(Long id) {
        // findById restituisce Optional: o c'è la razza, o lanciamo un errore
        // chiaro. orElseThrow evita di maneggiare null manualmente.
        Race race = raceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Razza non trovata con id " + id));

        // Conversione Entity → DTO completa. Avviene dentro la transazione,
        // quindi l'accesso a subRaces/classes/parent (lazy) è sicuro.
        return raceMapper.toDto(race);
    }

    /**
     * Restituisce l'ELENCO delle razze in forma sintetica (vista RaceSummaryDto).
     *
     * SCELTA DIDATTICA — perché qui RaceSummaryDto e non RaceDto:
     * in una lista non servono tutti i dettagli (mappe di bonus, sottoraze...):
     * basta id, nome, taglia, velocità. Usare il DTO "leggero" rende la risposta
     * più piccola, più veloce e senza relazioni da caricare. È lo stesso DTO
     * "foglia" che nel mapper serviva a spezzare i cicli: qui lo riusiamo come
     * vista da lista.
     *
     * Convertiamo elemento per elemento con uno stream chiamando raceMapper::toSummary.
     */
    @Transactional(readOnly = true)
    public List<RaceSummaryDto> getAll() {
        return raceRepository.findAll()
                .stream()
                .map(raceMapper::toSummary)
                .toList();
    }

    /**
     * Variante che elenca SOLO le razze base (senza le sottorazze), sfruttando
     * la query derivata del repository. Mostra come la logica di "quali dati
     * leggere" sta nel repository, mentre la logica di "come presentarli" (DTO)
     * sta qui nel service.
     */
    @Transactional(readOnly = true)
    public List<RaceSummaryDto> getBaseRaces() {
        return raceRepository.findByParentIsNull()
                .stream()
                .map(raceMapper::toSummary)
                .toList();
    }
}