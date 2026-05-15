package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "trucks")
@Setter
@Getter
public class Truck extends BaseEntity {

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private Short year;

    @Column(name = "vin")
    private String vin;

    @Column(name = "next_service_date")
    private LocalDate nextServiceDate;

    @Column(name = "euro_standard", nullable = false)
    @Enumerated(EnumType.STRING)
    private EuroStandard euroStandard = EuroStandard.EURO_6;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TruckStatus status = TruckStatus.ACTIVE;

    @Column(name = "notes")
    private String notes;


}
