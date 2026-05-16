package com.trucknest.backend.invoices.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MarkPaidRequest(

        @NotNull(message = "Payment date is required")
        LocalDate paymentDate,

        @NotNull(message = "Amount paid is required")
        BigDecimal amountPaid
) {}