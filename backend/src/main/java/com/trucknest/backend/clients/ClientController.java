package com.trucknest.backend.clients;

import com.trucknest.backend.clients.dto.ClientRequest;
import com.trucknest.backend.clients.dto.ClientResponse;
import com.trucknest.backend.clients.dto.TransportJobRequest;
import com.trucknest.backend.clients.dto.TransportJobResponse;
import com.trucknest.backend.clients.internal.ClientService;
import com.trucknest.backend.clients.internal.TransportJobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final TransportJobService transportJobService;

    public ClientController(ClientService clientService, TransportJobService transportJobService) {
        this.clientService = clientService;
        this.transportJobService = transportJobService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable UUID id,
                                                       @RequestBody @Valid ClientRequest request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/transport-jobs")
    public ResponseEntity<List<TransportJobResponse>> getTransportJobs(@PathVariable UUID id) {
        return ResponseEntity.ok(transportJobService.getTransportJobsByClient(id));
    }

    @PostMapping("/{id}/transport-jobs")
    public ResponseEntity<TransportJobResponse> createTransportJob(
            @PathVariable UUID id,
            @RequestBody @Valid TransportJobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportJobService.createTransportJob(id, request));
    }
}
