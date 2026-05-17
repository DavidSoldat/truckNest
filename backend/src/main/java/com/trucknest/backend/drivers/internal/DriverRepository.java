package com.trucknest.backend.drivers.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {

    List<Driver> findAllByCompanyId(UUID companyId);

    Optional<Driver> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Driver> findAllByCompanyIdAndLicenseExpiryBefore(UUID companyId, LocalDate date);

    List<Driver> findAllByCompanyIdAndVisaExpiryBefore(UUID companyId, LocalDate date);

    List<Driver> findAllByLicenseExpiryBefore(LocalDate date);

    List<Driver> findAllByVisaExpiryBefore(LocalDate date);
}