package com.trucknest.backend.trucks.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceRecordResponse(
        UUID id,
        UUID truckId,
        LocalDate serviceDate,
        String serviceType,
        BigDecimal cost,
        Integer odometerKm,
        LocalDate nextServiceDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}