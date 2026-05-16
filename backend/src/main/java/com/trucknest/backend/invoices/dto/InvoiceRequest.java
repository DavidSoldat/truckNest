package com.trucknest.backend.invoices.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceRequest(

        @NotNull(message = "Client ID is required")
        UUID clientId,

        UUID transportJobId,

        @NotBlank(message = "Invoice number is required")
        String invoiceNumber,

        @NotNull(message = "Issue date is required")
        LocalDate issueDate,

        @NotNull(message = "Amount is required")
        BigDecimal amount,

        String notes
) {}