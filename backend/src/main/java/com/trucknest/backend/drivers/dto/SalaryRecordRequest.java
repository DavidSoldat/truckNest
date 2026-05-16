package com.trucknest.backend.drivers.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryRecordRequest(

        @NotNull(message = "Period month is required")
        Short periodMonth,

        @NotNull(message = "Period year is required")
        Short periodYear,

        @NotNull(message = "Amount paid is required")
        BigDecimal amountPaid,

        @NotNull(message = "Payment date is required")
        LocalDate paymentDate,

        String notes
) {}