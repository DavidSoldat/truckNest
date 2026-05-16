package com.trucknest.backend.drivers.dto;

import com.trucknest.backend.drivers.internal.DriverStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record DriverResponse(
        UUID id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phone,
        String email,
        String licenseNumber,
        LocalDate licenseExpiry,
        LocalDate visaExpiry,
        DriverStatus status,
        BigDecimal monthlySalary,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
