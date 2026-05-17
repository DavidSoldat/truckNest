package com.trucknest.backend.notifications.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentExpiryEvent {
    private UUID driverId;
    private String driverName;
    private String documentType;
    private LocalDate expiryDate;
    private UUID companyId;
    private String ownerEmail;
}