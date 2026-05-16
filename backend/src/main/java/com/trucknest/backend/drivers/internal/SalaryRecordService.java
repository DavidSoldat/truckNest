package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.drivers.dto.SalaryRecordRequest;
import com.trucknest.backend.drivers.dto.SalaryRecordResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalaryRecordService {

    private final SalaryRecordRepository salaryRecordRepository;
    private final DriverRepository driverRepository;
    private final SalaryRecordMapper salaryRecordMapper;

    public SalaryRecordService(SalaryRecordRepository salaryRecordRepository, DriverRepository driverRepository, SalaryRecordMapper salaryRecordMapper) {
        this.salaryRecordRepository = salaryRecordRepository;
        this.driverRepository = driverRepository;
        this.salaryRecordMapper = salaryRecordMapper;
    }

    public SalaryRecordResponse addSalaryRecord(UUID driverId, SalaryRecordRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Driver driver = driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        SalaryRecord record = new SalaryRecord();
        record.setCompanyId(companyId);
        record.setDriver(driver);
        record.setPeriodMonth(request.periodMonth());
        record.setPeriodYear(request.periodYear());
        record.setAmountPaid(request.amountPaid());
        record.setPaymentDate(request.paymentDate());
        record.setNotes(request.notes());

        return salaryRecordMapper.toResponse(salaryRecordRepository.save(record));
    }

    public List<SalaryRecordResponse> getSalaryRecords(UUID driverId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return salaryRecordMapper.toResponseList(
                salaryRecordRepository
                        .findAllByDriverIdAndCompanyIdOrderByPeriodYearDescPeriodMonthDesc(
                                driverId, companyId)
        );
    }
}
