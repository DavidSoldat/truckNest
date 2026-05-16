package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.drivers.dto.IncidentRequest;
import com.trucknest.backend.drivers.dto.IncidentResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final DriverRepository driverRepository;
    private final IncidentMapper incidentMapper;

    public IncidentService(IncidentRepository incidentRepository, DriverRepository driverRepository, IncidentMapper incidentMapper) {
        this.incidentRepository = incidentRepository;
        this.driverRepository = driverRepository;
        this.incidentMapper = incidentMapper;
    }

    public IncidentResponse addIncident(UUID driverId, IncidentRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Driver driver = driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        Incident incident = new Incident();
        incident.setCompanyId(companyId);
        incident.setDriver(driver);
        incident.setIncidentDate(request.incidentDate());
        incident.setIncidentType(request.incidentType());
        incident.setDescription(request.description());
        incident.setCost(request.cost());

        return incidentMapper.toResponse(incidentRepository.save(incident));
    }

    public List<IncidentResponse> getIncidents(UUID driverId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        driverRepository.findByIdAndCompanyId(driverId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        return incidentMapper.toResponseList(
                incidentRepository.findAllByDriverIdAndCompanyIdOrderByIncidentDateDesc(
                        driverId, companyId)
        );
    }
}