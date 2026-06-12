package it.dnd.game_elements_service.dto;

import it.dnd.game_elements_service.entity.enumerate.CreatureSize;

/**
 * Vista "leggera" (riepilogo) di una {@link it.dnd.game_elements_service.entity.Race}.
 *
 * RUOLO NELLA ROTTURA DEI CICLI:
 * Questa è la classe più importante del pattern. È volutamente "piatta":
 * contiene SOLO campi scalari e NESSUNA relazione (niente subRaces, niente
 * parent, niente classi favorite). È il punto in cui il grafo dei DTO smette
 * di ramificarsi.
 *
 * PERCHÉ ESISTE:
 * Nell'entity Race la relazione padre/figlio è bidirezionale e auto-referenziale
 * (parent ↔ subRaces) e c'è anche il ManyToMany con le classi. Se il DTO completo
 * (RaceDto) usasse un altro RaceDto per ogni sottoRaza, la serializzazione
 * (Jackson) o il mapping (MapStruct) ricorrerebbero all'infinito:
 *   Elf → subRaces → Wood Elf → parent → Elf → subRaces → ...
 * Usando invece RaceSummaryDto per le sottoraze, la catena si ferma qui:
 * una RaceSummaryDto non ha a sua volta sottoraze né parent, quindi non può
 * tornare indietro verso la razza madre. Il ciclo è "spezzato".
 *
 * USO TIPICO:
 *  - elementi della lista subRaces dentro RaceDto
 *  - risultati di endpoint "lista razze" dove non servono tutti i dettagli
 *
 * SCELTA DEL RECORD:
 * I DTO di sola lettura sono ottimi candidati per i record Java: immutabili,
 * niente boilerplate (getter/equals/hashCode/toString generati dal compilatore),
 * niente rischio di toString ricorsivo perché non contengono relazioni cicliche.
 * In alternativa si poteva usare una classe con Lombok, come per le entity.
 */
public record RaceSummaryDto(
        Long id,
        String name,
        CreatureSize size,
        Double speed
) {
}