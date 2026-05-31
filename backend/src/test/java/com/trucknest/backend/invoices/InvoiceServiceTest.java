package com.trucknest.backend.invoices;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.invoices.dto.InvoiceRequest;
import com.trucknest.backend.invoices.dto.InvoiceResponse;
import com.trucknest.backend.invoices.dto.MarkPaidRequest;
import com.trucknest.backend.invoices.internal.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceMapper invoiceMapper;
    @InjectMocks
    private InvoiceService invoiceService;

    private static final UUID COMPANY_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final UUID INVOICE_ID = UUID.randomUUID();

    private MockedStatic<TenantContext> tenantContextMock;

    @BeforeEach
    void setUpTenantContext() {
        tenantContextMock = mockStatic(TenantContext.class);
        tenantContextMock.when(TenantContext::getTenantId).thenReturn(COMPANY_ID.toString());
    }

    @AfterEach
    void tearDownTenantContext() {
        tenantContextMock.close();
    }

    private Invoice buildInvoice(InvoiceStatus status) {
        Invoice invoice = new Invoice();
        invoice.setClientId(CLIENT_ID);
        invoice.setInvoiceNumber("INV-2026-001");
        invoice.setIssueDate(LocalDate.of(2026, 1, 1));
        invoice.setDueDate(LocalDate.of(2026, 1, 31));
        invoice.setAmount(new BigDecimal("1500.00"));
        invoice.setStatus(status);
        return invoice;
    }

    private InvoiceResponse buildResponse(Invoice invoice) {
        return new InvoiceResponse(
                INVOICE_ID,
                invoice.getClientId(),
                null,
                invoice.getInvoiceNumber(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getAmount(),
                invoice.getStatus(),
                invoice.getPaymentDate(),
                invoice.getAmountPaid(),
                invoice.getReminderSentAt(),
                invoice.getNotes(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Nested
    class CreateInvoice {

        @Test
        @DisplayName("should create invoice with pending status and correct due date")
        void shouldCreateInvoice() {
            InvoiceRequest request = new InvoiceRequest(
                    CLIENT_ID,
                    null,
                    "INV-TEST-00",
                    LocalDate.of(2026,5,5),
                    new BigDecimal("1500.00"),
                    null
            );
            Invoice savedInvoice = buildInvoice(InvoiceStatus.PENDING);
            when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);
            when(invoiceMapper.toResponse(savedInvoice)).thenReturn(buildResponse(savedInvoice));
            InvoiceResponse result = invoiceService.createInvoice(request, (short) 30);

            assertThat(result).isNotNull();
            assertThat(result.status()).isEqualTo(InvoiceStatus.PENDING);

            ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
            verify(invoiceRepository).save(captor.capture());
            Invoice captured = captor.getValue();
            assertThat(captured.getDueDate()).isEqualTo(LocalDate.of(2026, 6, 4));
            assertThat(captured.getStatus()).isEqualTo(InvoiceStatus.PENDING);
            assertThat(captured.getCompanyId()).isEqualTo(COMPANY_ID);
        }

        @Test
        @DisplayName("should set company ID from TenantContext")
        void shouldSetCompanyId() {
            InvoiceRequest request = new InvoiceRequest(
                    CLIENT_ID,
                    null,
                    "INV-TEST-00",
                    LocalDate.of(2026,5,5),
                    new BigDecimal("1500.00"),
                    null
            );

            Invoice savedInvoice = buildInvoice(InvoiceStatus.PENDING);
            when(invoiceRepository.save(any())).thenReturn(savedInvoice);
            when(invoiceMapper.toResponse(any())).thenReturn(buildResponse(savedInvoice));

            invoiceService.createInvoice(request, (short) 14);

            ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
            verify(invoiceRepository).save(captor.capture());
            assertThat(captor.getValue().getCompanyId()).isEqualTo(COMPANY_ID);
        }

        @Test
        @DisplayName("should calculate due date correctly for 45 day payment terms")
        void shouldCalculateDueDateCorrectlyFor45Days() {
            LocalDate issueDate = LocalDate.of(2026, 3, 1);
            InvoiceRequest request = new InvoiceRequest(
                    CLIENT_ID, null, "INV-002",
                    issueDate, new BigDecimal("200.00"), null
            );
            Invoice savedInvoice = buildInvoice(InvoiceStatus.PENDING);
            when(invoiceRepository.save(any())).thenReturn(savedInvoice);
            when(invoiceMapper.toResponse(any())).thenReturn(buildResponse(savedInvoice));

            invoiceService.createInvoice(request, (short) 45);

            ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
            verify(invoiceRepository).save(captor.capture());
            assertThat(captor.getValue().getDueDate()).isEqualTo(LocalDate.of(2026, 4, 15));
        }
    }

    @Nested
    @DisplayName("getAllInvoices")
    class GetAllInvoices {

        @Test
        @DisplayName("should return all invoices with no status filter")
        void shouldReturnAllInvoicesWithoutFilter() {
            List<Invoice> invoices = List.of(
                    buildInvoice(InvoiceStatus.PENDING),
                    buildInvoice(InvoiceStatus.PAID)
            );
            List<InvoiceResponse> responses = invoices.stream()
                    .map(InvoiceServiceTest.this::buildResponse)
                    .toList();

            when(invoiceRepository.findAllByCompanyIdOrderByIssueDateDesc(COMPANY_ID))
                    .thenReturn(invoices);
            when(invoiceMapper.toResponseList(invoices)).thenReturn(responses);

            List<InvoiceResponse> result = invoiceService.getAllInvoices(null);

            assertThat(result).hasSize(2);
            verify(invoiceRepository).findAllByCompanyIdOrderByIssueDateDesc(COMPANY_ID);
            verify(invoiceRepository, never())
                    .findAllByCompanyIdAndStatusOrderByIssueDateDesc(any(), any());
        }

        @Test
        @DisplayName("should filter by status with status provided")
        void shouldFilterByStatusWhenProvided() {
            List<Invoice> pendingInvoices = List.of(buildInvoice(InvoiceStatus.PENDING));
            List<InvoiceResponse> responses = pendingInvoices.stream()
                    .map(InvoiceServiceTest.this::buildResponse)
                    .toList();

            when(invoiceRepository.findAllByCompanyIdAndStatusOrderByIssueDateDesc(
                    COMPANY_ID, InvoiceStatus.PENDING))
                    .thenReturn(pendingInvoices);
            when(invoiceMapper.toResponseList(pendingInvoices)).thenReturn(responses);

            List<InvoiceResponse> result = invoiceService.getAllInvoices(InvoiceStatus.PENDING);

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().status()).isEqualTo(InvoiceStatus.PENDING);
            verify(invoiceRepository).findAllByCompanyIdAndStatusOrderByIssueDateDesc(
                    COMPANY_ID, InvoiceStatus.PENDING);
            verify(invoiceRepository, never())
                    .findAllByCompanyIdOrderByIssueDateDesc(any());
        }
    }

    @Nested
    @DisplayName("markAsPaid")
    class MarkAsPaid {

        @Test
        @DisplayName("should mark invoice as PAID and set payment details")
        void shouldMarkInvoiceAsPaidWithPaymentDetails() {
            Invoice invoice = buildInvoice(InvoiceStatus.PENDING);
            MarkPaidRequest request = new MarkPaidRequest(
                    LocalDate.of(2026, 2, 1), new BigDecimal("1500.00")
            );

            when(invoiceRepository.findByIdAndCompanyId(INVOICE_ID, COMPANY_ID))
                    .thenReturn(Optional.of(invoice));
            when(invoiceRepository.save(invoice)).thenReturn(invoice);
            when(invoiceMapper.toResponse(invoice)).thenReturn(buildResponse(invoice));

            invoiceService.markAsPaid(INVOICE_ID, request);

            ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
            verify(invoiceRepository).save(captor.capture());
            Invoice saved = captor.getValue();
            assertThat(saved.getStatus()).isEqualTo(InvoiceStatus.PAID);
            assertThat(saved.getPaymentDate()).isEqualTo(LocalDate.of(2026, 2, 1));
            assertThat(saved.getAmountPaid()).isEqualByComparingTo("1500.00");
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when there is no invoice")
        void shouldThrowWhenInvoiceNotFound() {
            when(invoiceRepository.findByIdAndCompanyId(INVOICE_ID, COMPANY_ID))
                    .thenReturn(Optional.empty());

            MarkPaidRequest request = new MarkPaidRequest(
                    LocalDate.now(), new BigDecimal("1500.00")
            );

            assertThatThrownBy(() -> invoiceService.markAsPaid(INVOICE_ID, request))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Invoice not found");

            verify(invoiceRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("cancelInvoice")
    class CancelInvoice {

        @Test
        @DisplayName("should set status to CANCELLED")
        void shouldSetStatusToCancelled() {
            Invoice invoice = buildInvoice(InvoiceStatus.PENDING);

            when(invoiceRepository.findByIdAndCompanyId(INVOICE_ID, COMPANY_ID))
                    .thenReturn(Optional.of(invoice));
            when(invoiceRepository.save(invoice)).thenReturn(invoice);
            when(invoiceMapper.toResponse(invoice)).thenReturn(buildResponse(invoice));

            invoiceService.cancelInvoice(INVOICE_ID);

            ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
            verify(invoiceRepository).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(InvoiceStatus.CANCELLED);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when there is no invoice")
        void shouldThrowWhenInvoiceNotFound() {
            when(invoiceRepository.findByIdAndCompanyId(INVOICE_ID, COMPANY_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> invoiceService.cancelInvoice(INVOICE_ID))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Invoice not found");

            verify(invoiceRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("markOverdueInvoices")
    class MarkOverdueInvoices {

        @Test
        @DisplayName("should mark all pending overdue invoices")
        void shouldMarkAllPendingOverdueInvoices() {
            Invoice inv1 = buildInvoice(InvoiceStatus.PENDING);
            Invoice inv2 = buildInvoice(InvoiceStatus.PENDING);
            List<Invoice> overdueInvoices = List.of(inv1, inv2);

            when(invoiceRepository.findAllPendingOverdue(
                    eq(InvoiceStatus.PENDING), any(LocalDate.class)))
                    .thenReturn(overdueInvoices);

            invoiceService.markOverdueInvoices();

            assertThat(inv1.getStatus()).isEqualTo(InvoiceStatus.OVERDUE);
            assertThat(inv2.getStatus()).isEqualTo(InvoiceStatus.OVERDUE);
            verify(invoiceRepository).saveAll(overdueInvoices);
        }

        @Test
        @DisplayName("shouldnt do anything when there are no pending overdue invoices")
        void shouldDoNothingWhenNoOverdueInvoices() {
            when(invoiceRepository.findAllPendingOverdue(
                    eq(InvoiceStatus.PENDING), any(LocalDate.class)))
                    .thenReturn(List.of());

            invoiceService.markOverdueInvoices();

            verify(invoiceRepository).saveAll(List.of());
        }
    }
}
