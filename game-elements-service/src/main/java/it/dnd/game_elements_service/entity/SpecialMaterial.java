package it.dnd.game_elements_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta un materiale speciale con cui possono essere costruite armi e armature.
 *
 * In D&D 3.5 certi materiali conferiscono proprietà magiche o meccaniche
 * intrinseche, indipendentemente da eventuali incantamenti aggiuntivi.
 * Il loro utilizzo principale è bypassare certi tipi di Damage Reduction (DR).
 *
 * MATERIALI PRINCIPALI (PHB + DMG):
 *   Cold Iron      → bypassa DR/cold iron (demoni, diavoli). Costo +2000 gp per arma.
 *   Alchemical Silver → bypassa DR/silver (licantropi, diavoli). Penalità -1 danno.
 *   Adamantine     → bypassa DR/adamantine e DR/-. Durezza 20. Costo molto elevato.
 *   Mithral        → leggero come acciaio di categoria inferiore. Armature e scudi.
 *                    Un'armatura media in mithral conta come leggera per le competenze.
 *   Darkwood       → solo oggetti in legno (scudi, archi). Leggero come mithral.
 *   Dragonhide     → da dragoni, per armature. Conta come naturale.
 *
 * RELAZIONE CON ITEM:
 * {@link Item} ha un @ManyToOne opzionale verso SpecialMaterial.
 * Null = materiale standard (acciaio, cuoio).
 * Valorizzato = oggetto costruito con questo materiale speciale.
 *
 * costGp in Item tiene già il prezzo totale finale (base + extra del materiale).
 * I campi weaponCostBonus e armorCostBonus qui indicano il costo aggiuntivo
 * rispetto alla versione base, utile per il service che genera prezzi automaticamente.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome del materiale (es. "Cold Iron", "Adamantine", "Mithral")
    @Column(nullable = false, unique = true)
    private String name;

    // Descrizione degli effetti meccanici e delle proprietà generali
    @Column(length = 3000)
    private String description;

    // Durezza del materiale (resistenza ai danni diretti all'oggetto).
    // Acciaio standard: 10. Adamantine: 20. Mithral: 15.
    private Integer hardness;

    // Punti ferita per pollice di spessore dell'oggetto
    private Integer hpPerInch;

    // Costo aggiuntivo in gp per costruire un'arma con questo materiale.
    // Es. Cold Iron: +2000 gp. Adamantine: +3000 gp. Null = non applicabile alle armi.
    private Integer weaponCostBonus;

    // Costo aggiuntivo in gp per costruire un'armatura con questo materiale.
    // Es. Mithral: ×9 del prezzo base (approssimato in costBonus). Null = non applicabile.
    private Integer armorCostBonus;

    // Note specifiche per l'uso come arma (proprietà, penalità, limitazioni).
    // Es. "Silver: -1 al danno rispetto all'arma base in acciaio"
    @Column(length = 1000)
    private String weaponNotes;

    // Note specifiche per l'uso come armatura/scudo.
    // Es. "Mithral: conta come categoria inferiore per determinare la competenza richiesta"
    @Column(length = 1000)
    private String armorNotes;

    @Column(length = 200)
    private String sourceBook;
}