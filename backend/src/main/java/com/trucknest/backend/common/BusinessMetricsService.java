package com.trucknest.backend.common;

import com.trucknest.backend.drivers.DriverQueryService;
import com.trucknest.backend.invoices.InvoiceQueryService;
import com.trucknest.backend.trucks.TruckQueryService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class BusinessMetricsService {

    private final TruckQueryService truckQueryService;
    private final DriverQueryService driverQueryService;
    private final InvoiceQueryService invoiceQueryService;

    private final MeterRegistry meterRegistry;

    public BusinessMetricsService(TruckQueryService truckQueryService, DriverQueryService driverQueryService, InvoiceQueryService invoiceQueryService, MeterRegistry meterRegistry) {
        this.truckQueryService = truckQueryService;
        this.driverQueryService = driverQueryService;
        this.invoiceQueryService = invoiceQueryService;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void registerMetrics() {
        Gauge.builder("trucknest.trucks.service.due.soon", this,
                        BusinessMetricsService::countTrucksServiceDueSoon)
                .description("Trucks with service due in next 14 days")
                .register(meterRegistry);

        Gauge.builder("trucknest.documents.expiring.soon", this,
                        BusinessMetricsService::countDocumentsExpiringSoon)
                .description("Driver documents expiring in next 30 days")
                .register(meterRegistry);

        Gauge.builder("trucknest.invoices.overdue.total", this,
                        BusinessMetricsService::countOverdueInvoices)
                .description("Number of currently overdue invoices")
                .register(meterRegistry);
    }

    private double countTrucksServiceDueSoon() {
        try {
            return truckQueryService
                    .findTrucksWithServiceDueBefore(LocalDate.now().plusDays(14))
                    .size();
        } catch (Exception e) {
            log.error("Failed to count trucks with service due", e);
            return 0;
        }
    }

    private double countDocumentsExpiringSoon() {
        try {
            LocalDate threshold = LocalDate.now().plusDays(30);
            return driverQueryService.findDriversWithLicenseExpiryBefore(threshold).size()
                    + driverQueryService.findDriversWithVisaExpiryBefore(threshold).size();
        } catch (Exception e) {
            log.error("Failed to count expiring documents", e);
            return 0;
        }
    }

    private double countOverdueInvoices() {
        try {
            return invoiceQueryService
                    .findAllOverdueInvoices(LocalDate.now())
                    .size();
        } catch (Exception e) {
            log.error("Failed to count overdue invoices", e);
            return 0;
        }
    }
}