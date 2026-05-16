package com.trucknest.backend.clients;

import com.trucknest.backend.clients.internal.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientQueryService {

    private final ClientRepository clientRepository;

    public ClientQueryService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Short getPaymentTermsDays(UUID clientId, UUID companyId) {
        return clientRepository.findByIdAndCompanyId(clientId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"))
                .getPaymentTermsDays();
    }
}