package com.trucknest.backend.common.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TruckServiceDueDto(
        UUID id,
        String plateNumber,
        LocalDate nextServiceDate,
        LocalDate serviceDueNotifiedFor,
        UUID companyId
) {}