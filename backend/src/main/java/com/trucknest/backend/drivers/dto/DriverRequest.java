package com.trucknest.backend.drivers.dto;

import com.trucknest.backend.drivers.internal.DriverStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DriverRequest(

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        LocalDate dateOfBirth,

        @Pattern(regexp = "^\\+?[0-9\\s\\-()]{6,20}$", message = "Invalid phone number format")
        String phone,

        @Email(message = "Invalid email format")
        String email,

        String licenseNumber,
        LocalDate licenseExpiry,
        LocalDate visaExpiry,
        DriverStatus status,
        BigDecimal monthlySalary,
        String notes
) {}