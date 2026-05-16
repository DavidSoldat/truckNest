package com.trucknest.backend.drivers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SalaryRecordResponse(
        UUID id,
        UUID driverId,
        Short periodMonth,
        Short periodYear,
        BigDecimal amountPaid,
        LocalDate paymentDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}