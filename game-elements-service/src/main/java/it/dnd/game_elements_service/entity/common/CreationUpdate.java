package it.dnd.game_elements_service.entity.common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Estende {@link Creation} aggiungendo il timestamp dell'ultimo aggiornamento.
 *
 * @UpdateTimestamp: Hibernate aggiorna automaticamente il valore a ogni UPDATE,
 * senza che il service debba farlo manualmente.
 *
 * QUANDO USARLA:
 * Va estesa dalle entity che rappresentano dati modificabili dall'utente o
 * dall'amministratore nel ciclo di vita dell'applicazione:
 *   - Race       → le razze possono ricevere correzioni o aggiunte
 *   - Item       → gli oggetti possono essere aggiornati (costo, descrizione)
 *   - Language   → le lingue possono essere arricchite
 *
 * NON va usata per i dati statici di sistema (ClassCharacter, Spell, Feat)
 * perché quelli vengono gestiti tramite migrazioni o seed data, non via API.
 */
@MappedSuperclass
@Getter
@Setter
public class CreationUpdate extends Creation implements Serializable {

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}