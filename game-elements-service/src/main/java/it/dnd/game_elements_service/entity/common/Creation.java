package it.dnd.game_elements_service.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Superclasse base che aggiunge il timestamp di creazione a tutte le entity che la estendono.
 *
 * @MappedSuperclass: non genera una tabella propria. I suoi campi vengono
 * "ereditati" e aggiunti fisicamente alla tabella della sottoclasse da Hibernate.
 *
 * @CreationTimestamp: Hibernate imposta automaticamente il valore al momento
 * dell'INSERT. updatable=false impedisce che venga mai sovrascritto da UPDATE.
 *
 * Serializable: necessario per entità che potrebbero essere messe in cache
 * di secondo livello (Ehcache, Redis) o serializzate in sessioni HTTP.
 *
 * GERARCHIA:
 *   Creation  →  CreationUpdate  →  Race, Item, Language  (entità modificabili)
 *
 * Le entity di sola lettura (seed data come ClassCharacter, Spell, Feat) non
 * estendono questa classe perché non richiedono tracciatura temporale.
 */
@MappedSuperclass
@Getter
@Setter
public class Creation implements Serializable {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}