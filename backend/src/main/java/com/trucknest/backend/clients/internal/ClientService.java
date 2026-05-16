package com.trucknest.backend.clients.internal;

import com.trucknest.backend.clients.dto.ClientRequest;
import com.trucknest.backend.clients.dto.ClientResponse;
import com.trucknest.backend.common.tenant.TenantContext;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse createClient(ClientRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());

        Client client = new Client();
        client.setCompanyId(companyId);
        client.setName(request.name());
        client.setContactPerson(request.contactPerson());
        client.setContactEmail(request.contactEmail());
        client.setPhone(request.phone());
        client.setPaymentTermsDays(request.paymentTermsDays());
        client.setNotes(request.notes());

        return clientMapper.toResponse(clientRepository.save(client));
    }

    public List<ClientResponse> getAllClients() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return clientMapper.toResponseList(clientRepository.findAllByCompanyId(companyId));
    }

    public ClientResponse getClientById(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        return clientMapper.toResponse(
                clientRepository.findByIdAndCompanyId(id, companyId)
                        .orElseThrow(() -> new EntityNotFoundException("Client not found"))
        );
    }

    public ClientResponse updateClient(UUID id, ClientRequest request) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Client client = clientRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        client.setName(request.name());
        client.setContactPerson(request.contactPerson());
        client.setContactEmail(request.contactEmail());
        client.setPhone(request.phone());
        client.setPaymentTermsDays(request.paymentTermsDays());
        client.setNotes(request.notes());

        return clientMapper.toResponse(clientRepository.save(client));
    }

    public void deleteClient(UUID id) {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Client client = clientRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        clientRepository.delete(client);
    }
}