package com.trucknest.backend.trucks.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, UUID> {

    List<ServiceRecord> findAllByTruckIdAndCompanyIdOrderByServiceDateDesc(UUID truckId, UUID companyId);

    UUID truck(Truck truck);
}
