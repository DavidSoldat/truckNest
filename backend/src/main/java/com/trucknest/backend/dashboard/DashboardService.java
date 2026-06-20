package com.trucknest.backend.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trucknest.backend.common.dto.DriverDocumentDto;
import com.trucknest.backend.common.dto.InvoiceOverdueDto;
import com.trucknest.backend.common.dto.TruckServiceDueDto;
import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.drivers.DriverQueryService;
import com.trucknest.backend.drivers.internal.DriverRepository;
import com.trucknest.backend.invoices.InvoiceQueryService;
import com.trucknest.backend.trucks.TruckQueryService;
import com.trucknest.backend.trucks.internal.TruckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class DashboardService {

    private final TruckQueryService truckQueryService;
    private final DriverQueryService driverQueryService;
    private final InvoiceQueryService invoiceQueryService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public DashboardService(TruckQueryService truckQueryService, DriverQueryService driverQueryService, InvoiceQueryService invoiceQueryService, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.truckQueryService = truckQueryService;
        this.driverQueryService = driverQueryService;
        this.invoiceQueryService = invoiceQueryService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String CACHE_KEY_PREFIX = "dashboard:";
    private static final long CACHE_TTL_SECONDS = 60;

    public DashboardResponse getDashboard() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        String cacheKey = CACHE_KEY_PREFIX + companyId;

        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.info("Dashboard cache hit for company {}", companyId);
                return objectMapper.convertValue(cached, DashboardResponse.class);
            }
        } catch (Exception e) {
            log.warn("Redis cache read failed, falling back to database", e);
        }

        log.info("Dashboard cache miss for company {}", companyId);
        DashboardResponse response = buildDashboard(companyId);

        try {
            redisTemplate.opsForValue().set(cacheKey, response,
                    Duration.ofSeconds(CACHE_TTL_SECONDS));
        } catch (Exception e) {
            log.warn("Redis cache write failed", e);
        }

        return response;
    }

    private DashboardResponse buildDashboard(UUID companyId) {
        LocalDate today = LocalDate.now();

        List<TruckServiceDueDto> servicesDue = truckQueryService
                .findTrucksWithServiceDueBeforeForCompany(companyId, today.plusDays(14));

        List<DriverDocumentDto> documentsExpiring = Stream.concat(
                driverQueryService.findDriversWithLicenseExpiryBeforeForCompany(companyId, today.plusDays(30)).stream(),
                driverQueryService.findDriversWithVisaExpiryBeforeForCompany(companyId, today.plusDays(30)).stream()
        ).distinct().toList();

        List<InvoiceOverdueDto> overdueInvoices = invoiceQueryService
                .findOverdueInvoicesForCompany(companyId);

        long totalTrucks = truckQueryService.countByCompanyId(companyId);
        long totalDrivers = driverQueryService.countByCompanyId(companyId);

        BigDecimal pendingTotal = invoiceQueryService.getPendingInvoicesTotal(companyId);
        long pendingCount = invoiceQueryService.getPendingInvoicesCount(companyId);

        DashboardStats stats = new DashboardStats(
                totalTrucks,
                totalDrivers,
                pendingCount,
                pendingTotal
        );

        return new DashboardResponse(servicesDue, documentsExpiring, overdueInvoices, stats);
    }
}
