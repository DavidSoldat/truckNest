package com.trucknest.backend.trucks.dto;

import com.trucknest.backend.trucks.internal.TirePosition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TireResponse(
        UUID id,
        UUID truckId,
        TirePosition position,
        String brand,
        LocalDate fitDate,
        LocalDate expectedReplacementDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}