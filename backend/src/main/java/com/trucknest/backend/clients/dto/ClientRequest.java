package com.trucknest.backend.clients.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequest(
        @NotBlank(message = "Name is required")
        String name,

        String contactPerson,
        String contactEmail,
        String phone,

        @NotNull(message = "Payment terms are required")
        Short paymentTermsDays,

        String notes
) {}