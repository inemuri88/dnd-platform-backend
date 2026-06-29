package it.dnd.game_elements_service.service;

import it.dnd.game_elements_service.dto.RaceDto;
import it.dnd.game_elements_service.dto.RaceSummaryDto;
import it.dnd.game_elements_service.entity.Race;
import it.dnd.game_elements_service.entity.enumerate.CreatureSize;
import it.dnd.game_elements_service.mapper.RaceMapper;
import it.dnd.game_elements_service.repository.RaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * UNIT TEST di esempio per {@link RaceService} (JUnit 5 + Mockito).
 *
 * COS'È UN "UNIT TEST" (vs test d'integrazione):
 * Qui NON avviamo Spring, NON tocchiamo il database, NON carichiamo MapStruct.
 * Verifichiamo SOLO la logica di RaceService in isolamento, sostituendo i suoi
 * collaboratori (repository e mapper) con dei "mock", cioè finti oggetti
 * controllati da noi. È veloce (millisecondi) e fallisce solo se è sbagliata la
 * logica del service, non l'infrastruttura intorno.
 *
 * LE ANNOTAZIONI CHIAVE:
 *   - @ExtendWith(MockitoExtension.class): attiva Mockito su JUnit 5, così le
 *     annotazioni @Mock/@InjectMocks vengono processate prima di ogni test.
 *   - @Mock: crea un finto RaceRepository / RaceMapper. Di default ogni metodo
 *     restituisce null/lista vuota finché non glielo istruiamo con when(...).
 *   - @InjectMocks: istanzia il VERO RaceService e gli inietta nel costruttore
 *     i due @Mock qui sopra (constructor injection, la stessa che usa Spring).
 *
 * SCHEMA "AAA" che seguiamo in ogni test:
 *   Arrange  → prepariamo i dati e istruiamo i mock (when... thenReturn...)
 *   Act      → invochiamo il metodo del service sotto test
 *   Assert   → verifichiamo il risultato (assertThat) e/o le interazioni (verify)
 */
@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;

    @Mock
    private RaceMapper raceMapper;

    @InjectMocks
    private RaceService raceService;

    @Test
    @DisplayName("getById: quando la razza esiste, la converte in DTO col mapper")
    void getById_whenFound_returnsMappedDto() {
        // Arrange: una razza finta nel repository e il DTO che il mapper produrrà.
        long id = 1L;
        Race elf = new Race();
        elf.setId(id);
        elf.setName("Elf");

        // RaceDto è un record con molti campi: ne valorizziamo qualcuno e lasciamo
        // null/empty il resto. Non importa il contenuto: ci basta che il service
        // restituisca ESATTAMENTE l'oggetto prodotto dal mapper, senza alterarlo.
        RaceDto expectedDto = new RaceDto(
                id, "Elf", 9.0, CreatureSize.MEDIUM,
                Map.of(), Map.of(), Map.of(),
                null, null,
                List.of(), List.of(),
                null, null);

        // Istruiamo i mock: il repository "trova" la razza, il mapper la converte.
        when(raceRepository.findById(id)).thenReturn(Optional.of(elf));
        when(raceMapper.toDto(elf)).thenReturn(expectedDto);

        // Act
        RaceDto result = raceService.getById(id);

        // Assert: il service ha delegato a repository e mapper e ha restituito il DTO.
        assertThat(result).isSameAs(expectedDto);
        verify(raceRepository).findById(id);
        verify(raceMapper).toDto(elf);
    }

    @Test
    @DisplayName("getById: quando la razza NON esiste, lancia EntityNotFoundException")
    void getById_whenMissing_throws() {
        // Arrange: il repository non trova nulla.
        long id = 99L;
        when(raceRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert: ci aspettiamo l'eccezione, col messaggio che contiene l'id.
        assertThatThrownBy(() -> raceService.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        // Se la razza non c'è, il mapper non deve mai essere invocato.
        verifyNoInteractions(raceMapper);
    }

    @Test
    @DisplayName("getAll: converte ogni entity in RaceSummaryDto preservando l'ordine")
    void getAll_mapsEachToSummary() {
        // Arrange: due razze dal repository e i rispettivi summary dal mapper.
        Race elf = new Race();
        elf.setId(1L);
        elf.setName("Elf");
        Race dwarf = new Race();
        dwarf.setId(2L);
        dwarf.setName("Dwarf");

        var elfDto = new RaceSummaryDto(1L, "Elf", CreatureSize.MEDIUM, 9.0);
        var dwarfDto = new RaceSummaryDto(2L, "Dwarf", CreatureSize.MEDIUM, 6.0);

        when(raceRepository.findAll()).thenReturn(List.of(elf, dwarf));
        when(raceMapper.toSummary(elf)).thenReturn(elfDto);
        when(raceMapper.toSummary(dwarf)).thenReturn(dwarfDto);

        // Act
        List<RaceSummaryDto> result = raceService.getAll();

        // Assert: stessa quantità, stesso ordine, contenuto atteso.
        assertThat(result).containsExactly(elfDto, dwarfDto);
        verify(raceRepository).findAll();
    }

    @Test
    @DisplayName("getBaseRaces: usa la query findByParentIsNull del repository")
    void getBaseRaces_usesParentIsNullQuery() {
        // Arrange
        Race elf = new Race();
        elf.setId(1L);
        var elfDto = new RaceSummaryDto(1L, "Elf", CreatureSize.MEDIUM, 9.0);

        when(raceRepository.findByParentIsNull()).thenReturn(List.of(elf));
        when(raceMapper.toSummary(elf)).thenReturn(elfDto);

        // Act
        List<RaceSummaryDto> result = raceService.getBaseRaces();

        // Assert: deleghiamo alla query derivata corretta, non a findAll().
        assertThat(result).containsExactly(elfDto);
        verify(raceRepository).findByParentIsNull();
    }
}
