package com.trucknest.backend.trucks.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TruckServiceDueDto(
        UUID id,
        String plateNumber,
        LocalDate nextServiceDate,
        UUID companyId
) {}