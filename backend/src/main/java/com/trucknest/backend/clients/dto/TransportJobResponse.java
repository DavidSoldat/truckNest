package com.trucknest.backend.clients.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransportJobResponse(
        UUID id,
        UUID clientId,
        UUID truckId,
        UUID driverId,
        LocalDate jobDate,
        String origin,
        String destination,
        String cargoDescription,
        Integer distanceKm,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}