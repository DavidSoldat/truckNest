package com.trucknest.backend.drivers.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record KmLogResponse(
        UUID id,
        UUID driverId,
        UUID truckId,
        LocalDate logDate,
        Integer kmDriven,
        String routeNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}