package com.trucknest.backend.invoices;

import com.trucknest.backend.clients.ClientQueryService;
import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.invoices.dto.InvoiceRequest;
import com.trucknest.backend.invoices.dto.InvoiceResponse;
import com.trucknest.backend.invoices.dto.MarkPaidRequest;
import com.trucknest.backend.invoices.internal.InvoiceService;
import com.trucknest.backend.invoices.internal.InvoiceStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ClientQueryService clientQueryService;

    public InvoiceController(InvoiceService invoiceService, ClientQueryService clientQueryService) {
        this.invoiceService = invoiceService;
        this.clientQueryService = clientQueryService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices(
            @RequestParam(required = false) InvoiceStatus status) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody @Valid InvoiceRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Short paymentTermsDays = clientQueryService.getPaymentTermsDays(
                request.clientId(), companyId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invoiceService.createInvoice(request, paymentTermsDays));
    }

    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<InvoiceResponse> markAsPaid(@PathVariable UUID id,
                                                      @RequestBody @Valid MarkPaidRequest request) {
        return ResponseEntity.ok(invoiceService.markAsPaid(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<InvoiceResponse> cancelInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.cancelInvoice(id));
    }

    @PostMapping("/{id}/send-reminder")
    public ResponseEntity<Void> sendReminder(@PathVariable UUID id) {
        invoiceService.updateReminderSentAt(id);
        return ResponseEntity.ok().build();
    }
}