package com.trucknest.backend.notifications;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scheduler")
@Profile("local")
public class SchedulerTestController {

    private final ScheduledJobService scheduledJobService;

    public SchedulerTestController(ScheduledJobService scheduledJobService) {
        this.scheduledJobService = scheduledJobService;
    }

    @PostMapping("/trigger-service-due")
    public ResponseEntity<String> triggerServiceDue() {
        scheduledJobService.checkServiceDue();
        return ResponseEntity.ok("Service due check triggered");
    }

    @PostMapping("/trigger-document-expiry")
    public ResponseEntity<String> triggerDocumentExpiry() {
        scheduledJobService.checkDocumentExpiry();
        return ResponseEntity.ok("Document expiry check triggered");
    }

    @PostMapping("/trigger-overdue-invoices")
    public ResponseEntity<String> triggerOverdueInvoices() {
        scheduledJobService.checkOverdueInvoices();
        return ResponseEntity.ok("Overdue invoice check triggered");
    }
}
