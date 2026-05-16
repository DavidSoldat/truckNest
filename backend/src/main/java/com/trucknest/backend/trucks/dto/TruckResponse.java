package com.trucknest.backend.trucks.dto;

import com.trucknest.backend.trucks.internal.EuroStandard;
import com.trucknest.backend.trucks.internal.TruckStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TruckResponse(
        UUID id,
        String plateNumber,
        String make,
        String model,
        Short year,
        String vin,
        LocalDate nextServiceDate,
        TruckStatus status,
        EuroStandard euroStandard,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
