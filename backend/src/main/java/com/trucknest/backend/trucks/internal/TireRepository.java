package com.trucknest.backend.trucks.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TireRepository extends JpaRepository<Tire, UUID> {

    List<Tire> findAllByTruckIdAndCompanyId(UUID truckId, UUID companyId);

    Optional<Tire> findByIdAndCompanyId(UUID id, UUID companyId);
}
