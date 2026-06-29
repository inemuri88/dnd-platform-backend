package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.utilityspell.DomainSpellEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un Dominio del Cleric (Domain) in D&D 3.5.
 *
 * Il Cleric sceglie 2 domini al 1° livello (concessi dalla propria divinità).
 * Ogni dominio fornisce due cose:
 *   1. Una GRANTED POWER: capacità speciale usabile dal Cleric (es. nel dominio
 *      Fire può usare "Fire Resistance" o lanciare "Burning Hands" come bonus).
 *   2. DOMAIN SPELLS: uno spell aggiuntivo per ogni livello di spell (1–9),
 *      sempre preparabile indipendentemente dalla lista Cleric standard.
 *
 * SPELL DI DOMINIO:
 * Ogni dominio ha esattamente 9 spell (uno per livello 1–9), modellati tramite
 * {@link DomainSpellEntry} (bridge Domain + Spell + spellLevel).
 * Si usa un'entity ponte invece di un array perché:
 *   a) la relazione porta l'attributo "livello" dello spell nel dominio
 *   b) gli spell sono entity riutilizzabili già presenti nel catalogo
 *
 * RELAZIONE CON DEITY:
 * {@link Deity} ha un @ManyToMany verso Domain: una divinità offre un certo
 * insieme di domini tra cui il Cleric può scegliere.
 * Es. Pelor (dio del sole): domini Air, Good, Healing, Strength.
 *
 * ESEMPI DI DOMINI (PHB):
 *   Air, Animal, Chaos, Death, Destruction, Earth, Evil, Fire,
 *   Good, Healing, Knowledge, Law, Luck, Magic, Plant, Protection,
 *   Strength, Sun, Travel, Trickery, War, Water
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome del dominio (es. "Fire", "War", "Knowledge")
    @Column(nullable = false, unique = true)
    private String name;

    // Capacità speciale concessa al Cleric che sceglie questo dominio.
    // Può essere un bonus passivo, un'azione attiva o un modificatore agli spell.
    @Column(length = 2000)
    private String grantedPower;

    @Column(length = 200)
    private String sourceBook;

    // I 9 spell di dominio (uno per livello 1–9), ordinati per livello.
    // @OrderBy garantisce che arrivino già ordinati senza sort nel service.
    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("spellLevel ASC")
    private List<DomainSpellEntry> domainSpells = new ArrayList<>();
}