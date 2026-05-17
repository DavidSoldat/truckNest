package com.trucknest.backend.invoices;


import com.trucknest.backend.common.dto.InvoiceOverdueDto;
import com.trucknest.backend.invoices.internal.Invoice;
import com.trucknest.backend.invoices.internal.InvoiceRepository;
import com.trucknest.backend.invoices.internal.InvoiceStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<InvoiceOverdueDto> findPendingOverdueInvoices(LocalDate date) {
        return invoiceRepository.findAllPendingOverdue(InvoiceStatus.PENDING, date)
                .stream()
                .map(invoice -> new InvoiceOverdueDto(
                        invoice.getId(),
                        invoice.getInvoiceNumber(),
                        invoice.getClientId(),
                        invoice.getDueDate(),
                        invoice.getAmount(),
                        invoice.getCompanyId()
                ))
                .toList();
    }

    public void markAsOverdue(UUID invoiceId) {
        invoiceRepository.findById(invoiceId).ifPresent(invoice -> {
            invoice.setStatus(InvoiceStatus.OVERDUE);
            invoiceRepository.save(invoice);
        });
    }

    public BigDecimal getPendingInvoicesTotal(UUID companyId) {
        return invoiceRepository.findAllByCompanyIdAndStatusOrderByIssueDateDesc(
                        companyId, InvoiceStatus.PENDING)
                .stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getPendingInvoicesCount(UUID companyId) {
        return invoiceRepository.findAllByCompanyIdAndStatusOrderByIssueDateDesc(
                companyId, InvoiceStatus.PENDING).size();
    }
}