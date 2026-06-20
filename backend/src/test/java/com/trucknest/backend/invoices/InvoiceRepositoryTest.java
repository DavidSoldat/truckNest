package com.trucknest.backend.invoices;

import com.trucknest.backend.common.entity.Company;
import com.trucknest.backend.invoices.internal.Invoice;
import com.trucknest.backend.invoices.internal.InvoiceRepository;
import com.trucknest.backend.invoices.internal.InvoiceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InvoiceRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean("auditorAwareImpl")
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("test-user");
        }
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @Autowired
    private TestEntityManager em;
    @Autowired
    private InvoiceRepository invoiceRepository;


    @Test
    @DisplayName("should return only PENDING invoices with due date before today")
    void shouldReturnOnlyPendingOverdueInvoices() {
        Company company = new Company();
        company.setName("Test Company");
        company.setContactEmail("test@example.com");
        Company savedCompany = em.persist(company);
        em.flush();

        UUID companyId = savedCompany.getId();

        Invoice overdue = buildInvoice(companyId, InvoiceStatus.PENDING,
                LocalDate.now().minusDays(35), LocalDate.now().minusDays(5));
        Invoice notYetDue = buildInvoice(companyId, InvoiceStatus.PENDING,
                LocalDate.now().minusDays(20), LocalDate.now().plusDays(10));
        Invoice paid = buildInvoice(companyId, InvoiceStatus.PAID,
                LocalDate.now().minusDays(33), LocalDate.now().minusDays(3));

        em.persist(overdue);
        em.persist(notYetDue);
        em.persist(paid);
        em.flush();

        List<Invoice> result = invoiceRepository.findAllOverdue (
                InvoiceStatus.PENDING, LocalDate.now());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStatus()).isEqualTo(InvoiceStatus.PENDING);
        assertThat(result.getFirst().getDueDate()).isBefore(LocalDate.now());
    }

    @Test
    @DisplayName("should return only invoices by the tenant company")
    void shouldReturnOnlyInvoicesByTenantCompany() {
        Company company1 = new Company();
        company1.setName("Test Company");
        company1.setContactEmail("test@example.com");
        Company savedCompany1 = em.persist(company1);
        Company company2 = new Company();
        company2.setName("Test Company 2");
        company2.setContactEmail("test2@example.com");
        Company savedCompany2 = em.persist(company2);
        em.flush();

        UUID companyId1 = savedCompany1.getId();
        UUID companyId2 = savedCompany2.getId();

        Invoice inv = buildInvoice(savedCompany2.getId(), InvoiceStatus.PENDING,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));

        em.persist(inv);
        em.flush();

        Optional<Invoice> result1 = invoiceRepository.findByIdAndCompanyId(inv.getId(), companyId1);
        Optional<Invoice> result2 = invoiceRepository.findByIdAndCompanyId(inv.getId(), companyId2);

        assertThat(result1).isEmpty();
        assertThat(result2.isEmpty()).isFalse();
        assertThat(result2.get().getStatus()).isEqualTo(InvoiceStatus.PENDING);
    }

    @Test
    @DisplayName("should return only PENDING invoices ordered by issue date descending")
    void shouldReturnAllInvoicesByCompanyAndStatus() {
        Company company = new Company();
        company.setName("Test Company");
        company.setContactEmail("test@example.com");
        Company savedCompany = em.persist(company);
        em.flush();

        UUID companyId = savedCompany.getId();

        Invoice olderPending = buildInvoice(companyId, InvoiceStatus.PENDING,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        Invoice newerPending = buildInvoice(companyId, InvoiceStatus.PENDING,
                LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        Invoice paid = buildInvoice(companyId, InvoiceStatus.PAID,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(10));

        em.persist(olderPending);
        em.persist(newerPending);
        em.persist(paid);
        em.flush();

        List<Invoice> result = invoiceRepository.findAllByCompanyIdAndStatusOrderByIssueDateDesc(companyId, InvoiceStatus.PENDING);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(i -> i.getStatus() == InvoiceStatus.PENDING);

        assertThat(result.getFirst().getIssueDate())
                .isAfter(result.getLast().getIssueDate());
    }

    private Invoice buildInvoice(UUID companyId, InvoiceStatus status,
                                 LocalDate issueDate, LocalDate dueDate) {
        Invoice invoice = new Invoice();
        invoice.setCompanyId(companyId);
        invoice.setClientId(UUID.randomUUID());
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID());
        invoice.setIssueDate(issueDate);
        invoice.setDueDate(dueDate);
        invoice.setAmount(new BigDecimal("1000.00"));
        invoice.setStatus(status);
        return invoice;
    }
}