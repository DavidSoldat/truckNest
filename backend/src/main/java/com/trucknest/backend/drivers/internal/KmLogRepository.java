package com.trucknest.backend.drivers.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KmLogRepository extends JpaRepository<KmLog, UUID> {

    List<KmLog> findAllByDriverIdAndCompanyIdOrderByLogDateDesc(UUID driverId, UUID companyId);

    @Query("SELECT COALESCE(SUM(k.kmDriven), 0) FROM KmLog k WHERE k.driver.id = :driverId AND k.companyId = :companyId AND MONTH(k.logDate) = :month AND YEAR(k.logDate) = :year")
    Integer sumKmByDriverAndMonth(@Param("driverId") UUID driverId,
                                  @Param("companyId") UUID companyId,
                                  @Param("month") int month,
                                  @Param("year") int year);

    @Query("SELECT COALESCE(SUM(k.kmDriven), 0) FROM KmLog k WHERE k.driver.id = :driverId AND k.companyId = :companyId AND YEAR(k.logDate) = :year")
    Integer sumKmByDriverAndYear(@Param("driverId") UUID driverId,
                                 @Param("companyId") UUID companyId,
                                 @Param("year") int year);
}