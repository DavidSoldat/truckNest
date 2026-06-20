package com.trucknest.backend.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trucknest.backend.common.entity.CompanyQueryService;
import com.trucknest.backend.drivers.DriverQueryService;
import com.trucknest.backend.drivers.internal.DriverRepository;
import com.trucknest.backend.invoices.InvoiceQueryService;
import com.trucknest.backend.invoices.internal.InvoiceRepository;
import com.trucknest.backend.invoices.internal.InvoiceStatus;
import com.trucknest.backend.notifications.event.DocumentExpiryEvent;
import com.trucknest.backend.notifications.event.InvoiceOverdueEvent;
import com.trucknest.backend.notifications.event.KafkaTopics;
import com.trucknest.backend.notifications.event.ServiceDueEvent;
import com.trucknest.backend.trucks.TruckQueryService;
import com.trucknest.backend.trucks.internal.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class ScheduledJobService {

    private final TruckQueryService truckQueryService;
    private final DriverQueryService driverQueryService;
    private final InvoiceQueryService invoiceQueryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CompanyQueryService companyQueryService;

    public ScheduledJobService(TruckQueryService truckQueryService, DriverQueryService driverQueryService, InvoiceQueryService invoiceQueryService, KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper, CompanyQueryService companyQueryService) {
        this.truckQueryService = truckQueryService;
        this.driverQueryService = driverQueryService;
        this.invoiceQueryService = invoiceQueryService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.companyQueryService = companyQueryService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkServiceDue() {
        log.info("Running service due check");
        LocalDate threshold = LocalDate.now().plusDays(14);

        truckQueryService.findTrucksWithServiceDueBefore(threshold).forEach(truck -> {
            if (truck.nextServiceDate().equals(truck.serviceDueNotifiedFor())) {
                return;
            }
            try {
                ServiceDueEvent event = new ServiceDueEvent(
                        truck.id(),
                        truck.plateNumber(),
                        truck.nextServiceDate(),
                        truck.companyId(),
                        companyQueryService.getCompanyEmail(truck.companyId())
                );
                kafkaTemplate.send(KafkaTopics.SERVICE_DUE,
                        objectMapper.writeValueAsString(event));
                truckQueryService.markServiceDueNotified(truck.id(), truck.nextServiceDate());
                log.info("Emitted service due event for truck {}", truck.plateNumber());
            } catch (Exception e) {
                log.error("Failed to emit service due event for truck {}", truck.id(), e);
            }
        });
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkDocumentExpiry() {
        log.info("Running document expiry check");
        LocalDate threshold = LocalDate.now().plusDays(30);

        driverQueryService.findDriversWithLicenseExpiryBefore(threshold).forEach(driver -> {
            if (driver.licenseExpiry().equals(driver.licenseExpiryNotifiedFor())) {
                return;
            }
            try {
                DocumentExpiryEvent event = new DocumentExpiryEvent(
                        driver.id(),
                        driver.fullName(),
                        "LICENSE",
                        driver.licenseExpiry(),
                        driver.companyId(),
                        companyQueryService.getCompanyEmail(driver.companyId())
                );
                kafkaTemplate.send(KafkaTopics.DOCUMENT_EXPIRY,
                        objectMapper.writeValueAsString(event));
                driverQueryService.markLicenseExpiryNotified(driver.id(), driver.licenseExpiry());
            } catch (Exception e) {
                log.error("Failed to emit document expiry event for driver {}", driver.id(), e);
            }
        });

        driverQueryService.findDriversWithVisaExpiryBefore(threshold).forEach(driver -> {
            if (driver.visaExpiry().equals(driver.visaExpiryNotifiedFor())) {
                return;
            }
            try {
                DocumentExpiryEvent event = new DocumentExpiryEvent(
                        driver.id(),
                        driver.fullName(),
                        "VISA",
                        driver.visaExpiry(),
                        driver.companyId(),
                        companyQueryService.getCompanyEmail(driver.companyId())
                );
                kafkaTemplate.send(KafkaTopics.DOCUMENT_EXPIRY,
                        objectMapper.writeValueAsString(event));
                driverQueryService.markVisaExpiryNotified(driver.id(), driver.visaExpiry());
            } catch (Exception e) {
                log.error("Failed to emit document expiry event for driver {}", driver.id(), e);
            }
        });
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void checkOverdueInvoices() {
        log.info("Running overdue invoice check");

        invoiceQueryService.findPendingDueTodayInvoices(LocalDate.now()).forEach(invoice -> {
            try {
                invoiceQueryService.markAsOverdue(invoice.id());

                InvoiceOverdueEvent event = new InvoiceOverdueEvent(
                        invoice.id(),
                        invoice.invoiceNumber(),
                        invoice.clientId().toString(),
                        invoice.dueDate(),
                        invoice.amount(),
                        invoice.companyId(),
                        companyQueryService.getCompanyEmail(invoice.companyId())
                );
                kafkaTemplate.send(KafkaTopics.INVOICE_OVERDUE,
                        objectMapper.writeValueAsString(event));
            } catch (Exception e) {
                log.error("Failed to process overdue invoice {}", invoice.id(), e);
            }
        });
    }
}