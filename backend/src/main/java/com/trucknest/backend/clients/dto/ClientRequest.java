package com.trucknest.backend.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClientRequest(
        @NotBlank(message = "Name is required")
        String name,

        String contactPerson,

        @Email(message = "Invalid email format")
        String contactEmail,

        @Pattern(regexp = "^\\+?[0-9\\s\\-()]{6,20}$", message = "Invalid phone number format")
        String phone,

        @NotNull(message = "Payment terms are required")
        Short paymentTermsDays,

        String notes
) {}