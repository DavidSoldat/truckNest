package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "truck_tires")
@Getter
@Setter
public class Tire extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    private TirePosition position;

    @Column(name = "brand")
    private String brand;

    @Column(name = "fit_date")
    private LocalDate fitDate;

    @Column(name = "expected_replacement_date")
    private LocalDate expectedReplacementDate;

    @Column(name = "notes")
    private String notes;
}
