package com.trucknest.backend.drivers;

import com.trucknest.backend.drivers.dto.DriverDocumentDto;
import com.trucknest.backend.drivers.internal.DriverRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DriverQueryService {

    private final DriverRepository driverRepository;

    public DriverQueryService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<DriverDocumentDto> findDriversWithLicenseExpiryBefore(LocalDate date) {
        return driverRepository.findAllByLicenseExpiryBefore(date)
                .stream()
                .map(driver -> new DriverDocumentDto(
                        driver.getId(),
                        driver.getFirstName() + " " + driver.getLastName(),
                        driver.getLicenseExpiry(),
                        driver.getVisaExpiry(),
                        driver.getCompanyId()
                ))
                .toList();
    }

    public List<DriverDocumentDto> findDriversWithVisaExpiryBefore(LocalDate date) {
        return driverRepository.findAllByVisaExpiryBefore(date)
                .stream()
                .map(driver -> new DriverDocumentDto(
                        driver.getId(),
                        driver.getFirstName() + " " + driver.getLastName(),
                        driver.getLicenseExpiry(),
                        driver.getVisaExpiry(),
                        driver.getCompanyId()
                ))
                .toList();
    }
}