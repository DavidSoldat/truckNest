package com.trucknest.backend.clients;

import com.trucknest.backend.clients.dto.TransportJobResponse;
import com.trucknest.backend.clients.internal.TransportJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transport-jobs")
public class TransportJobController {

    private final TransportJobService transportJobService;

    public TransportJobController(TransportJobService transportJobService) {
        this.transportJobService = transportJobService;
    }

    @GetMapping
    public ResponseEntity<List<TransportJobResponse>> getAllTransportJobs() {
        return ResponseEntity.ok(transportJobService.getAllTransportJobs());
    }
}