package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import it.dnd.game_elements_service.entity.enumerate.ArmorCategory;
import it.dnd.game_elements_service.entity.enumerate.DamageType;
import it.dnd.game_elements_service.entity.enumerate.ItemCategory;
import it.dnd.game_elements_service.entity.enumerate.WeaponCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * Rappresenta un oggetto (Item) nel mondo di D&D 3.5: arma, armatura,
 * scudo, consumabile (pozione, pergamena), oggetto meraviglioso o strumento.
 *
 * CAMPI CONDIZIONALI:
 * Non tutti i campi sono rilevanti per ogni tipo di oggetto. Il service
 * deve valorizzare solo quelli pertinenti alla categoria:
 *
 *   WEAPON  → weaponCategory, damageType, damageMedium, critical, rangeIncrement
 *   ARMOR   → armorCategory, armorBonus, maxDexBonus, armorCheckPenalty, arcaneSpellFailure
 *   SHIELD  → armorCategory=SHIELD, armorBonus, armorCheckPenalty
 *   CONSUMABLE / WONDROUS / TOOL → solo i campi base (name, category, costGp, weight, description)
 *
 * CAMPO "damageMedium":
 * In D&D 3.5 il danno di un'arma scala con la taglia della creatura.
 * Si salva il danno per taglia MEDIUM (es. "1d8") e il service calcola
 * le varianti per taglia SMALL e LARGE (o LARGE e HUGE) quando serve.
 *
 * CAMPO "critical":
 * Descrive il moltiplicatore e il range di minaccia (es. "19-20/x2", "x3").
 * Si usa String perché la combinazione range/moltiplicatore è eterogenea.
 *
 * CAMPO "arcaneSpellFailure":
 * Percentuale di fallimento degli incantesimi arcani con componente Somatic
 * quando si indossa questa armatura (es. 25 per un'armatura media).
 * 0 o null per armi e oggetti non-armatura.
 *
 * @Builder: consente la costruzione fluida tramite builder pattern, utile nei test
 * e nel seed data dove non si vuole passare tutti i null esplicitamente.
 *
 * equals/hashCode su id + name: permette di confrontare Item in Set o prima
 * della persistenza (quando id è ancora null). Coerente con Language.
 *
 * Estende CreationUpdate: gli oggetti possono essere aggiornati dagli admin
 * (correzioni statistiche, nuovi oggetti, modifiche al costo).
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Item extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Categoria principale: determina quali campi sotto sono rilevanti
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    // Sottocategoria arma: SIMPLE/MARTIAL/EXOTIC × MELEE/RANGED
    @Enumerated(EnumType.STRING)
    private WeaponCategory weaponCategory;

    // Sottocategoria armatura: LIGHT, MEDIUM, HEAVY, SHIELD
    @Enumerated(EnumType.STRING)
    private ArmorCategory armorCategory;

    // Tipo di danno fisico dell'arma (SLASHING, PIERCING, BLUDGEONING o combo)
    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    // Danno per creatura di taglia MEDIUM (es. "1d8"). Null per non-armi.
    @Column
    private String damageMedium;

    // Range/moltiplicatore critico (es. "19-20/x2", "x3"). Null per non-armi.
    @Column
    private String critical;

    // Gittata incrementale in piedi (es. 30 per shortbow). Null per armi da mischia.
    @Column
    private Integer rangeIncrement;

    // Bonus alla CA conferito dall'armatura/scudo. Null per non-armature.
    @Column
    private Integer armorBonus;

    // Massimo bonus DEX applicabile alla CA quando si indossa questa armatura.
    // Null per armature leggere e oggetti non-armatura.
    @Column
    private Integer maxDexBonus;

    // Penalità ai tiri abilità fisiche (Climb, Jump, ecc.) per l'ingombro dell'armatura.
    @Column
    private Integer armorCheckPenalty;

    // Percentuale di fallimento per incantesimi arcani con componente Somatic.
    @Column
    private Integer arcaneSpellFailure;

    // Costo in monete d'oro (gp). Null = prezzo non definito (oggetto artigianale, ecc.)
    @Column
    private Integer costGp;

    // Peso in libbre. Usato per calcolare il carico portato dal personaggio.
    @Column(nullable = false)
    private Double weight;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(this.id, item.id) && Objects.equals(this.name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}