package com.trucknest.backend.drivers;

import com.trucknest.backend.drivers.dto.*;
import com.trucknest.backend.drivers.internal.DriverService;
import com.trucknest.backend.drivers.internal.IncidentService;
import com.trucknest.backend.drivers.internal.KmLogService;
import com.trucknest.backend.drivers.internal.SalaryRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
public class DriverController {

    private final DriverService driverService;
    private final KmLogService kmLogService;
    private final SalaryRecordService salaryRecordService;
    private final IncidentService incidentService;

    public DriverController(DriverService driverService, KmLogService kmLogService, SalaryRecordService salaryRecordService, IncidentService incidentService) {
        this.driverService = driverService;
        this.kmLogService = kmLogService;
        this.salaryRecordService = salaryRecordService;
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable UUID id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@RequestBody @Valid DriverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable UUID id, @RequestBody @Valid DriverRequest request) {
        return ResponseEntity.ok(driverService.updateDriver(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable UUID id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/km-logs")
    public ResponseEntity<List<KmLogResponse>> getKmLogs(@PathVariable UUID id) {
        return ResponseEntity.ok(kmLogService.getKmLogs(id));
    }

    @PostMapping("/{id}/km-logs")
    public ResponseEntity<KmLogResponse> addKmLog(@PathVariable UUID id,
                                                  @RequestBody @Valid KmLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kmLogService.addKmLog(id, request));
    }

    @GetMapping("/{id}/km-logs/monthly")
    public ResponseEntity<Integer> getMonthlyKm(@PathVariable UUID id,
                                                @RequestParam int month,
                                                @RequestParam int year) {
        return ResponseEntity.ok(kmLogService.getMonthlyKm(id, month, year));
    }

    @GetMapping("/{id}/km-logs/yearly")
    public ResponseEntity<Integer> getYearlyKm(@PathVariable UUID id,
                                               @RequestParam int year) {
        return ResponseEntity.ok(kmLogService.getYearlyKm(id, year));
    }

    @GetMapping("/{id}/salary-records")
    public ResponseEntity<List<SalaryRecordResponse>> getSalaryRecords(@PathVariable UUID id) {
        return ResponseEntity.ok(salaryRecordService.getSalaryRecords(id));
    }

    @PostMapping("/{id}/salary-records")
    public ResponseEntity<SalaryRecordResponse> addSalaryRecord(
            @PathVariable UUID id,
            @RequestBody @Valid SalaryRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salaryRecordService.addSalaryRecord(id, request));
    }

    @GetMapping("/{id}/incidents")
    public ResponseEntity<List<IncidentResponse>> getIncidents(@PathVariable UUID id) {
        return ResponseEntity.ok(incidentService.getIncidents(id));
    }

    @PostMapping("/{id}/incidents")
    public ResponseEntity<IncidentResponse> addIncident(
            @PathVariable UUID id,
            @RequestBody @Valid IncidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incidentService.addIncident(id, request));
    }
}
