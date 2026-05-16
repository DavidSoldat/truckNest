package com.trucknest.backend.trucks;

import com.trucknest.backend.trucks.dto.*;
import com.trucknest.backend.trucks.internal.ServiceRecordService;
import com.trucknest.backend.trucks.internal.TireService;
import com.trucknest.backend.trucks.internal.TruckService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trucks")
public class TruckController {

    private final TruckService truckService;
    private final ServiceRecordService serviceRecordService;
    private final TireService tireService;

    public TruckController(TruckService truckService, ServiceRecordService serviceRecordService, TireService tireService) {
        this.truckService = truckService;
        this.serviceRecordService = serviceRecordService;
        this.tireService = tireService;
    }

    @GetMapping
    public ResponseEntity<List<TruckResponse>> getAllTrucks() {
        return ResponseEntity.ok(truckService.getAllTrucks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TruckResponse> getTruckById(@PathVariable UUID id) {
        return  ResponseEntity.ok(truckService.getTruckById(id));
    }

    @PostMapping
    public ResponseEntity<TruckResponse> createTruck(@RequestBody @Valid TruckRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(truckService.createTruck(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TruckResponse> updateTruck(@PathVariable UUID id,
                                                     @RequestBody @Valid TruckRequest request) {
        return ResponseEntity.ok(truckService.updateTruck(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruck(@PathVariable UUID id) {
        truckService.deleteTruck(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/service-records")
    public ResponseEntity<List<ServiceRecordResponse>> getServiceRecords(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(serviceRecordService.getServiceRecords(id));
    }

    @PostMapping("/{id}/service-records")
    public ResponseEntity<ServiceRecordResponse> addServiceRecord(@PathVariable("id") UUID id,
                                                                  @RequestBody @Valid ServiceRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceRecordService.addServiceRecord(id, request));
    }

    @GetMapping("/{id}/tires")
    public ResponseEntity<List<TireResponse>> getTires(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(tireService.getTires(id));
    }

    @PostMapping("/{id}/tires")
    public ResponseEntity<TireResponse> addTire(@PathVariable("id") UUID id,
                                                @RequestBody @Valid TireRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tireService.addTire(id, request));
    }

    @PutMapping("/{id}/tires/{tireId}")
    public ResponseEntity<TireResponse> updateTire(@PathVariable("id") UUID id,
                                                   @PathVariable("tireId") UUID tireId,
                                                   @RequestBody @Valid TireRequest request) {
        return ResponseEntity.ok(tireService.updateTire(id, tireId, request));
    }
}
