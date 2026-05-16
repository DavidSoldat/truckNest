package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "truck_service_records")
@Getter
@Setter
public class ServiceRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "odometer_km")
    private Integer odometerKm;

    @Column(name = "next_service_date")
    private LocalDate nextServiceDate;

    @Column(name = "notes")
    private String notes;
}
