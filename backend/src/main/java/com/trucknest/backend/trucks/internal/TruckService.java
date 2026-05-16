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

    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
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

        Truck saved = truckRepository.save(truck);
        return toResponse(truck);
    }

    public List<TruckResponse> getAllTrucks() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return truckRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TruckResponse getTruckById(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Truck truck = truckRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));
        return toResponse(truck);
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

        Truck saved = truckRepository.save(truck);
        return toResponse(saved);
    }

    public void deleteTruck(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Truck truck = truckRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));
        truckRepository.delete(truck);
    }

//    temp here, USE MAPSTRUCT!
    private TruckResponse toResponse(Truck truck) {
        return new TruckResponse(
                truck.getId(),
                truck.getPlateNumber(),
                truck.getMake(),
                truck.getModel(),
                truck.getYear(),
                truck.getVin(),
                truck.getNextServiceDate(),
                truck.getStatus(),
                truck.getEuroStandard(),
                truck.getNotes(),
                truck.getCreatedAt(),
                truck.getUpdatedAt()
        );
    }
}
