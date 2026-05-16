package com.trucknest.backend.invoices.dto;

import com.trucknest.backend.invoices.internal.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        UUID clientId,
        UUID transportJobId,
        String invoiceNumber,
        LocalDate issueDate,
        LocalDate dueDate,
        BigDecimal amount,
        InvoiceStatus status,
        LocalDate paymentDate,
        BigDecimal amountPaid,
        LocalDateTime reminderSentAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}