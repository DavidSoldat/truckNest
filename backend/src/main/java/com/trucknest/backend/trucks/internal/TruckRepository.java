package com.trucknest.backend.trucks.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TruckRepository extends JpaRepository<Truck, UUID> {

    List<Truck> findAllByCompanyId(UUID companyId);

    Optional<Truck> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Truck> findAllByCompanyIdAndNextServiceDateBefore(UUID companyId, LocalDate date);

    List<Truck> findAllByNextServiceDateBefore(LocalDate date);

    @Modifying
    @Query("UPDATE Truck t SET t.serviceDueNotifiedFor = :date WHERE t.id = :id")
    void markServiceDueNotified(@Param("id") UUID id, @Param("date") LocalDate date);
}
