package com.trucknest.backend.drivers.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record KmLogRequest(

        @NotNull(message = "Log date is required")
        LocalDate logDate,

        @NotNull(message = "Km driven is required")
        Integer kmDriven,

        UUID truckId,
        String routeNotes
) {}
