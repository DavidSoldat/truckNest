package com.trucknest.backend.drivers.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DriverDocumentDto(
        UUID id,
        String fullName,
        LocalDate licenseExpiry,
        LocalDate visaExpiry,
        UUID companyId
) {}