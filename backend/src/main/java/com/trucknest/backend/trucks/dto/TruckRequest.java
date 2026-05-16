package com.trucknest.backend.trucks.dto;

import com.trucknest.backend.trucks.internal.EuroStandard;
import com.trucknest.backend.trucks.internal.TruckStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TruckRequest(
        @NotBlank(message = "Plate number is required")
        String plateNumber,

        @NotBlank(message = "Make is required")
        String make,

        @NotBlank(message = "Model is required")
        String model,

        @NotNull(message = "Year is required")
        Short year,

        String vin,
        LocalDate nextServiceDate,
        TruckStatus status,
        EuroStandard euroStandard,
        String notes
) {}
