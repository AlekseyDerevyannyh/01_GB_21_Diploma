package ru.gb.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Электропотребитель
 */
@Entity
@Table(name = "power_consumers")
@Data
public class PowerConsumer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                    // id потребителя

    @Column(name = "name")
    private String name;                // наименование потребителя

    @Column(name = "power_supply_category")
    private String powerSupplyCategory; // требуемая категория электроснабжения по ПУЭ

    @Column(name = "location")
    private String location;            // расположение потребителя (№ помещения на плане)

    @Column(name = "power")
    private Integer power;              // мощность, кВт

    @Column(name = "voltage")
    private Integer voltage;            // напряжение, В

    @Column(name = "power_factor")
    private Double powerFactor;         // коэффициент мощности (cos(Fi))

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
