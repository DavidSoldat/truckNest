package com.trucknest.backend.clients.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record TransportJobRequest(

        @NotNull(message = "Job date is required")
        LocalDate jobDate,

        UUID truckId,
        UUID driverId,
        String origin,
        String destination,
        String cargoDescription,
        Integer distanceKm,
        String notes
) {}