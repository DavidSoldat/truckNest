package com.trucknest.backend.invoices.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceOverdueDto(
        UUID id,
        String invoiceNumber,
        UUID clientId,
        LocalDate dueDate,
        BigDecimal amount,
        UUID companyId
) {}