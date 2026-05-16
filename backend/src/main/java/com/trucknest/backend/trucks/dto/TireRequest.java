package com.trucknest.backend.trucks.dto;

import com.trucknest.backend.trucks.internal.TirePosition;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TireRequest(

        @NotNull(message = "Position is required")
        TirePosition position,

        String brand,
        LocalDate fitDate,
        LocalDate expectedReplacementDate,
        String notes
) {}