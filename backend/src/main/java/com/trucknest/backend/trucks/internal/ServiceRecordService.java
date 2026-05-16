package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.trucks.dto.ServiceRecordRequest;
import com.trucknest.backend.trucks.dto.ServiceRecordResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;
    private final TruckRepository truckRepository;

    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository, TruckRepository truckRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
        this.truckRepository = truckRepository;
    }

    public ServiceRecordResponse addServiceRecord(UUID truckId, ServiceRecordRequest request) {
        UUID companyId  = UUID.fromString(TenantContext.getTenantId());

        Truck truck = truckRepository.findByIdAndCompanyId(truckId, companyId).orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        ServiceRecord record = new ServiceRecord();
        record.setCompanyId(companyId);
        record.setTruck(truck);
        record.setServiceDate(request.serviceDate());
        record.setServiceType(request.serviceType());
        record.setCost(request.cost());
        record.setOdometerKm(request.odometerKm());
        record.setNextServiceDate(request.nextServiceDate());
        record.setNotes(request.notes());

        if (request.nextServiceDate() != null) {
            truck.setNextServiceDate(request.nextServiceDate());
            truckRepository.save(truck);
        }

        ServiceRecord saved = serviceRecordRepository.save(record);
        return toResponse(saved);
    }

    public List<ServiceRecordResponse> getServiceRecords(UUID truckId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        truckRepository.findByIdAndCompanyId(truckId, companyId).orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        return serviceRecordRepository.findAllByTruckIdAndCompanyIdOrderByServiceDateDesc(truckId, companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ServiceRecordResponse toResponse(ServiceRecord record) {
        return new ServiceRecordResponse(
                record.getId(),
                record.getTruck().getId(),
                record.getServiceDate(),
                record.getServiceType(),
                record.getCost(),
                record.getOdometerKm(),
                record.getNextServiceDate(),
                record.getNotes(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
