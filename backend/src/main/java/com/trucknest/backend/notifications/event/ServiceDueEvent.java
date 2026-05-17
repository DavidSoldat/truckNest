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
public class ServiceDueEvent {
    private UUID truckId;
    private String plateNumber;
    private LocalDate dueDate;
    private UUID companyId;
    private String ownerEmail;
}