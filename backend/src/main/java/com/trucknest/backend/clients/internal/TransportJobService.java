package com.trucknest.backend.clients.internal;

import com.trucknest.backend.clients.dto.TransportJobRequest;
import com.trucknest.backend.clients.dto.TransportJobResponse;
import com.trucknest.backend.common.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransportJobService {

    private final TransportJobRepository transportJobRepository;
    private final ClientRepository clientRepository;
    private final TransportJobMapper transportJobMapper;

    public TransportJobService(TransportJobRepository transportJobRepository, ClientRepository clientRepository, TransportJobMapper transportJobMapper) {
        this.transportJobRepository = transportJobRepository;
        this.clientRepository = clientRepository;
        this.transportJobMapper = transportJobMapper;
    }

    public TransportJobResponse createTransportJob(UUID clientId, TransportJobRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Client client = clientRepository.findByIdAndCompanyId(clientId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        TransportJob job = new TransportJob();
        job.setCompanyId(companyId);
        job.setClient(client);
        job.setTruckId(request.truckId());
        job.setDriverId(request.driverId());
        job.setJobDate(request.jobDate());
        job.setOrigin(request.origin());
        job.setDestination(request.destination());
        job.setCargoDescription(request.cargoDescription());
        job.setDistanceKm(request.distanceKm());
        job.setNotes(request.notes());

        return transportJobMapper.toResponse(transportJobRepository.save(job));
    }

    public List<TransportJobResponse> getTransportJobsByClient(UUID clientId) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        clientRepository.findByIdAndCompanyId(clientId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        return transportJobMapper.toResponseList(
                transportJobRepository.findAllByClientIdAndCompanyIdOrderByJobDateDesc(
                        clientId, companyId)
        );
    }

    public List<TransportJobResponse> getAllTransportJobs() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return transportJobMapper.toResponseList(
                transportJobRepository.findAllByCompanyIdOrderByJobDateDesc(companyId)
        );
    }
}