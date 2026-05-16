package com.trucknest.backend.drivers.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalaryRecordRepository extends JpaRepository<SalaryRecord, UUID> {

    List<SalaryRecord> findAllByDriverIdAndCompanyIdOrderByPeriodYearDescPeriodMonthDesc(
            UUID driverId, UUID companyId);
}
