package com.trucknest.backend.trucks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ServiceRecordRequest(

        @NotNull(message = "Service date is required")
        LocalDate serviceDate,

        @NotBlank(message = "Service type is required")
        String serviceType,

        BigDecimal cost,
        Integer odometerKm,
        LocalDate nextServiceDate,
        String notes
) {}
