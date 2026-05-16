package com.trucknest.backend.drivers.dto;

import com.trucknest.backend.drivers.internal.DriverStatus;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DriverRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        LocalDate dateOfBirth,
        String phone,
        String email,
        String licenseNumber,
        LocalDate licenseExpiry,
        LocalDate visaExpiry,
        DriverStatus status,
        BigDecimal monthlySalary,
        String notes
) {}