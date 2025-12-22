package it.dnd.game_elements_service.entity;

import it.dnd.game_elements_service.entity.common.CreationUpdate;
import it.dnd.game_elements_service.entity.enumerate.ArmorCategory;
import it.dnd.game_elements_service.entity.enumerate.DamageType;
import it.dnd.game_elements_service.entity.enumerate.ItemCategory;
import it.dnd.game_elements_service.entity.enumerate.WeaponCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
}
