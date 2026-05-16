package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.drivers.dto.KmLogRequest;
import com.trucknest.backend.drivers.dto.KmLogResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KmLogService {

    private final KmLogRepository kmLogRepository;
    private final DriverRepository driverRepository;
    private final KmLogMapper kmLogMapper;

    public KmLogService(KmLogRepository kmLogRepository, DriverRepository driverRepository, KmLogMapper kmLogMapper) {
        this.kmLogRepository = kmLogRepository;
        this.driverRepository = driverRepository;
        this.kmLogMapper = kmLogMapper;
    }

    public KmLogResponse addKmLog(UUID driverId, KmLogRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Driver driver = driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        KmLog kmLog = new KmLog();
        kmLog.setCompanyId(companyId);
        kmLog.setDriver(driver);
        kmLog.setTruckId(request.truckId());
        kmLog.setLogDate(request.logDate());
        kmLog.setKmDriven(request.kmDriven());
        kmLog.setRouteNotes(request.routeNotes());

        return kmLogMapper.toResponse(kmLogRepository.save(kmLog));
    }

    public List<KmLogResponse> getKmLogs(UUID driverId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return kmLogMapper.toResponseList(
                kmLogRepository.findAllByDriverIdAndCompanyIdOrderByLogDateDesc(driverId, companyId)
        );
    }

    public Integer getMonthlyKm(UUID driverId, int month, int year) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return kmLogRepository.sumKmByDriverAndMonth(driverId, companyId, month, year);
    }

    public Integer getYearlyKm(UUID driverId, int year) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return kmLogRepository.sumKmByDriverAndYear(driverId, companyId, year);
    }
}
