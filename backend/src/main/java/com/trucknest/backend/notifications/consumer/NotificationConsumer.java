package com.trucknest.backend.notifications.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trucknest.backend.notifications.event.DocumentExpiryEvent;
import com.trucknest.backend.notifications.event.InvoiceOverdueEvent;
import com.trucknest.backend.notifications.event.KafkaTopics;
import com.trucknest.backend.notifications.event.ServiceDueEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    private final ObjectMapper objectMapper;

    public NotificationConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = KafkaTopics.SERVICE_DUE, groupId = "trucknest-notifications")
    public void handleServiceDue(String message) {
        try {
            ServiceDueEvent event = objectMapper.readValue(message, ServiceDueEvent.class);
            log.info("Received service due event for truck {} due on {}",
                    event.getPlateNumber(), event.getDueDate());
            // TODO: send email via Brevo
            sendEmail(
                    event.getOwnerEmail(),
                    "Service Due: Truck " + event.getPlateNumber(),
                    "Your truck " + event.getPlateNumber() +
                            " is due for service on " + event.getDueDate()
            );
        } catch (Exception e) {
            log.error("Failed to process service due event", e);
        }
    }

    @KafkaListener(topics = KafkaTopics.DOCUMENT_EXPIRY, groupId = "trucknest-notifications")
    public void handleDocumentExpiry(String message) {
        try {
            DocumentExpiryEvent event = objectMapper.readValue(message, DocumentExpiryEvent.class);
            log.info("Received document expiry event for driver {} - {} expires on {}",
                    event.getDriverName(), event.getDocumentType(), event.getExpiryDate());
            sendEmail(
                    event.getOwnerEmail(),
                    "Document Expiry: " + event.getDocumentType() + " for " + event.getDriverName(),
                    "Driver " + event.getDriverName() + "'s " + event.getDocumentType() +
                            " expires on " + event.getExpiryDate()
            );
        } catch (Exception e) {
            log.error("Failed to process document expiry event", e);
        }
    }

    @KafkaListener(topics = KafkaTopics.INVOICE_OVERDUE, groupId = "trucknest-notifications")
    public void handleInvoiceOverdue(String message) {
        try {
            InvoiceOverdueEvent event = objectMapper.readValue(message, InvoiceOverdueEvent.class);
            log.info("Received invoice overdue event for invoice {} amount {}",
                    event.getInvoiceNumber(), event.getAmount());
            sendEmail(
                    event.getOwnerEmail(),
                    "Invoice Overdue: " + event.getInvoiceNumber(),
                    "Invoice " + event.getInvoiceNumber() +
                            " for " + event.getClientName() +
                            " is overdue since " + event.getDueDate() +
                            ". Amount: " + event.getAmount()
            );
        } catch (Exception e) {
            log.error("Failed to process invoice overdue event", e);
        }
    }

    private void sendEmail(String to, String subject, String body) {
        // TODO: integrate Brevo SMTP
        log.info("EMAIL TO: {} | SUBJECT: {} | BODY: {}", to, subject, body);
    }
}