package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.trucks.dto.TireRequest;
import com.trucknest.backend.trucks.dto.TireResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TireService {

    private final TireRepository tireRepository;
    private final TruckRepository truckRepository;
    private final TireMapper tireMapper;

    public TireService(TireRepository tireRepository, TruckRepository truckRepository, TireMapper tireMapper) {
        this.tireRepository = tireRepository;
        this.truckRepository = truckRepository;
        this.tireMapper = tireMapper;
    }

    public TireResponse addTire(UUID truckId, TireRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Truck truck = truckRepository.findById(truckId).orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        Tire tire = new Tire();
        tire.setCompanyId(companyId);
        tire.setTruck(truck);
        tire.setPosition(request.position());
        tire.setBrand(request.brand());
        tire.setFitDate(request.fitDate());
        tire.setExpectedReplacementDate(request.expectedReplacementDate());
        tire.setNotes(request.notes());


        return tireMapper.toResponse(tireRepository.save(tire));
    }

    public TireResponse updateTire(UUID truckId, UUID tireId, TireRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        truckRepository.findByIdAndCompanyId(truckId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        Tire tire = tireRepository.findByIdAndCompanyId(tireId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Tire not found"));

        tire.setPosition(request.position());
        tire.setBrand(request.brand());
        tire.setFitDate(request.fitDate());
        tire.setExpectedReplacementDate(request.expectedReplacementDate());
        tire.setNotes(request.notes());

        return tireMapper.toResponse(tireRepository.save(tire));
    }

    public List<TireResponse> getTires(UUID truckId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        truckRepository.findByIdAndCompanyId(truckId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Truck not found"));

        return tireMapper.toResponseList(tireRepository.findAllByTruckIdAndCompanyId(truckId, companyId));
    }
}


