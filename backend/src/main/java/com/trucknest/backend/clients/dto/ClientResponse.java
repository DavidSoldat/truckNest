package com.trucknest.backend.clients.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(
        UUID id,
        String name,
        String contactPerson,
        String contactEmail,
        String phone,
        Short paymentTermsDays,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
