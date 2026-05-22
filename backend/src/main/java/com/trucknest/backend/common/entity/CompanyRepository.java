package com.trucknest.backend.common.entity;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByContactEmail(String contactEmail);

    @Modifying
    @Transactional
    @Query(value = "UPDATE companies SET company_id = id WHERE id = :id", nativeQuery = true)
    void updateCompanyId(@Param("id") UUID id);
}