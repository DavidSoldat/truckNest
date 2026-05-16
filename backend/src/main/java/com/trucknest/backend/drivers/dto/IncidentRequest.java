package com.trucknest.backend.drivers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncidentRequest(

        @NotNull(message = "Incident date is required")
        LocalDate incidentDate,

        @NotBlank(message = "Incident type is required")
        String incidentType,

        String description,
        BigDecimal cost
) {}