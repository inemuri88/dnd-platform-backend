package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import it.dnd.game_elements_service.entity.enumerate.ArmorCategory;
import it.dnd.game_elements_service.entity.enumerate.DamageType;
import it.dnd.game_elements_service.entity.enumerate.ItemCategory;
import it.dnd.game_elements_service.entity.enumerate.WeaponCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Enumerated(EnumType.STRING)
    private WeaponCategory weaponCategory;

    @Enumerated(EnumType.STRING)
    private ArmorCategory armorCategory;

    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    @Column
    private String damageMedium;

    @Column
    private String critical;

    @Column
    private Integer rangeIncrement;

    @Column
    private Integer armorBonus;

    @Column
    private Integer maxDexBonus;

    @Column
    private Integer armorCheckPenalty;

    @Column
    private Integer arcaneSpellFailure;

    @Column
    private Integer costGp;

    @Column(nullable = false)
    private Double weight;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id!=null && Objects.equals(this.id, item.id) && Objects.equals(this.name, item.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, name);
    }
}
