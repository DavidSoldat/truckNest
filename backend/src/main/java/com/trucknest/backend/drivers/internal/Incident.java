package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "driver_incidents")
@Getter
@Setter
public class Incident extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "incident_type", nullable = false)
    private String incidentType;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private BigDecimal cost;
}