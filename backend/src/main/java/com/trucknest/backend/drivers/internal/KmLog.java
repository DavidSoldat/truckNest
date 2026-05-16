package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "driver_km_logs")
@Getter
@Setter
public class KmLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "truck_id")
    private UUID truckId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "km_driven", nullable = false)
    private Integer kmDriven;

    @Column(name = "route_notes")
    private String routeNotes;
}