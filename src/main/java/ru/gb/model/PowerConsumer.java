package ru.gb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Электропотребитель
 */
@Entity
@Table(name = "POWER_CONSUMERS")
@Getter
@Setter
public class PowerConsumer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                    // идентификатор потребителя

    @Column(name = "name", nullable = false)
    private String name;                // наименование потребителя

    @Column(name = "power_supply_category", nullable = false)
    private String powerSupplyCategory; // требуемая категория электроснабжения по ПУЭ

    @Column(name = "location", nullable = false)
    private String location;            // расположение потребителя (№ помещения на плане)

    @Column(name = "power")
    private Integer power;              // мощность, Вт

    @Column(name = "voltage")
    private Integer voltage;            // напряжение, В

    @Column(name = "power_factor")
    private Double powerFactor;         // коэффициент мощности (cos(Fi))

    public PowerConsumer() {}
}
