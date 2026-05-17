package com.trucknest.backend.notifications.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceOverdueEvent {
    private UUID invoiceId;
    private String invoiceNumber;
    private String clientName;
    private LocalDate dueDate;
    private BigDecimal amount;
    private UUID companyId;
    private String ownerEmail;
}