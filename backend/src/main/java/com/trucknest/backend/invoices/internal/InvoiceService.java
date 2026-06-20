package com.trucknest.backend.invoices.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.invoices.dto.InvoiceRequest;
import com.trucknest.backend.invoices.dto.InvoiceResponse;
import com.trucknest.backend.invoices.dto.MarkPaidRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    public InvoiceResponse createInvoice(InvoiceRequest request, Short paymentTermsDays) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Invoice invoice = new Invoice();
        invoice.setCompanyId(companyId);
        invoice.setClientId(request.clientId());
        invoice.setTransportJobId(request.transportJobId());
        invoice.setInvoiceNumber(request.invoiceNumber());
        invoice.setIssueDate(request.issueDate());
        invoice.setDueDate(request.issueDate().plusDays(paymentTermsDays));
        invoice.setAmount(request.amount());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setNotes(request.notes());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    public List<InvoiceResponse> getAllInvoices(InvoiceStatus status) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        if (status != null) {
            return invoiceMapper.toResponseList(
                    invoiceRepository.findAllByCompanyIdAndStatusOrderByIssueDateDesc(
                            companyId, status)
            );
        }

        return invoiceMapper.toResponseList(
                invoiceRepository.findAllByCompanyIdOrderByIssueDateDesc(companyId)
        );
    }

    public InvoiceResponse getInvoiceById(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return invoiceMapper.toResponse(
                invoiceRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Invoice not found"))
        );
    }

    public InvoiceResponse markAsPaid(UUID id, MarkPaidRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Invoice invoice = invoiceRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentDate(request.paymentDate());
        invoice.setAmountPaid(request.amountPaid());

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    public InvoiceResponse cancelInvoice(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Invoice invoice = invoiceRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.CANCELLED);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    public void markOverdueInvoices() {
        List<Invoice> overdueInvoices = invoiceRepository
                .findAllOverdue(InvoiceStatus.PENDING, LocalDate.now());

        overdueInvoices.forEach(invoice -> invoice.setStatus(InvoiceStatus.OVERDUE));
        invoiceRepository.saveAll(overdueInvoices);
    }

    public InvoiceResponse updateReminderSentAt(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Invoice invoice = invoiceRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        invoice.setReminderSentAt(LocalDateTime.now());
        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }
}