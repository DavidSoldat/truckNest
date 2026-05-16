package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.drivers.dto.DriverRequest;
import com.trucknest.backend.drivers.dto.DriverResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    public DriverResponse createDriver(DriverRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Driver driver = new Driver();
        driver.setCompanyId(companyId);
        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setDateOfBirth(request.dateOfBirth());
        driver.setPhone(request.phone());
        driver.setEmail(request.email());
        driver.setLicenseNumber(request.licenseNumber());
        driver.setLicenseExpiry(request.licenseExpiry());
        driver.setVisaExpiry(request.visaExpiry());
        driver.setStatus(request.status() != null ? request.status() : DriverStatus.ACTIVE);
        driver.setMonthlySalary(request.monthlySalary());
        driver.setNotes(request.notes());

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    public List<DriverResponse> getAllDrivers() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return driverMapper.toResponseList(driverRepository.findAllByCompanyId(companyId));
    }

    public DriverResponse getDriverById(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        return driverMapper.toResponse(driverRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found")));
    }

    public DriverResponse updateDriver(UUID id, DriverRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Driver driver = driverRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        driver.setFirstName(request.firstName());
        driver.setLastName(request.lastName());
        driver.setDateOfBirth(request.dateOfBirth());
        driver.setPhone(request.phone());
        driver.setEmail(request.email());
        driver.setLicenseNumber(request.licenseNumber());
        driver.setLicenseExpiry(request.licenseExpiry());
        driver.setVisaExpiry(request.visaExpiry());
        driver.setStatus(request.status());
        driver.setMonthlySalary(request.monthlySalary());
        driver.setNotes(request.notes());

        return driverMapper.toResponse(driverRepository.save(driver));
    }

    public void deleteDriver(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Driver driver = driverRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        driverRepository.delete(driver);
    }
}
