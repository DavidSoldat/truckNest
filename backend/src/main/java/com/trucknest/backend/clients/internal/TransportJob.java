package com.trucknest.backend.clients.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transport_jobs")
@Getter
@Setter
public class TransportJob extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "truck_id")
    private UUID truckId;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "job_date", nullable = false)
    private LocalDate jobDate;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "cargo_description")
    private String cargoDescription;

    @Column(name = "distance_km")
    private Integer distanceKm;

    @Column(name = "notes")
    private String notes;
}