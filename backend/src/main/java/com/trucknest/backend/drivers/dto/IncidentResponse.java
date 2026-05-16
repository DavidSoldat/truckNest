package com.trucknest.backend.drivers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        UUID driverId,
        LocalDate incidentDate,
        String incidentType,
        String description,
        BigDecimal cost,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}