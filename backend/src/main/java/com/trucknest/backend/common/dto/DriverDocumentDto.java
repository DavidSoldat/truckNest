package com.trucknest.backend.common.dto;

import java.time.LocalDate;
import java.util.UUID;

public record DriverDocumentDto(
        UUID id,
        String fullName,
        LocalDate licenseExpiry,
        LocalDate visaExpiry,
        LocalDate licenseExpiryNotifiedFor,
        LocalDate visaExpiryNotifiedFor,
        UUID companyId
) {}