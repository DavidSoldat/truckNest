package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.trucks.dto.TruckRequest;
import com.trucknest.backend.trucks.dto.TruckResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ListTokenSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TruckService {

    private final TruckRepository truckRepository;
    private final TruckMapper truckMapper;

    public TruckService(TruckRepository truckRepository, TruckMapper truckMapper) {
        this.truckRepository = truckRepository;
        this.truckMapper = truckMapper;
    }

    public TruckResponse createTruck(TruckRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Truck truck = new Truck();
        truck.setCompanyId(companyId);
        truck.setPlateNumber(request.plateNumber());
        truck.setMake(request.make());
        truck.setModel(request.model());
        truck.setYear(request.year());
        truck.setVin(request.vin());
        truck.setNextServiceDate(request.nextServiceDate());
        truck.setStatus(request.status() != null ? request.status() : TruckStatus.ACTIVE);
        truck.setEuroStandard(request.euroStandard());
        truck.setNotes(request.notes());

        return truckMapper.toResponse(truckRepository.save(truck));
    }

    public List<TruckResponse> getAllTrucks() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return truckMapper.toResponseList(truckRepository.findAllByCompanyId(companyId));
    }

    public TruckResponse getTruckById(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return truckMapper.toResponse(
                truckRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Truck not found"))
        );
    }

    public TruckResponse updateTruck(UUID id, TruckRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Truck truck = truckRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        truck.setPlateNumber(request.plateNumber());
        truck.setMake(request.make());
        truck.setModel(request.model());
        truck.setYear(request.year());
        truck.setVin(request.vin());
        truck.setNextServiceDate(request.nextServiceDate());
        truck.setStatus(request.status());
        truck.setEuroStandard(request.euroStandard());
        truck.setNotes(request.notes());

        return truckMapper.toResponse(truckRepository.save(truck));
    }

    public void deleteTruck(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Truck truck = truckRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));
        truckRepository.delete(truck);
    }
}
