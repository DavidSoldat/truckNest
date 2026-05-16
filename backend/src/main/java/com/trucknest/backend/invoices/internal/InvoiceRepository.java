package com.trucknest.backend.invoices.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findAllByCompanyIdOrderByIssueDateDesc(UUID companyId);

    List<Invoice> findAllByCompanyIdAndStatusOrderByIssueDateDesc(UUID companyId, InvoiceStatus status);

    Optional<Invoice> findByIdAndCompanyId(UUID id, UUID companyId);

    List<Invoice> findAllByCompanyIdAndStatusAndDueDateBefore(
            UUID companyId, InvoiceStatus status, LocalDate date);

    @Query("SELECT i FROM Invoice i WHERE i.status = :status AND i.dueDate < :date")
    List<Invoice> findAllPendingOverdue(@Param("status") InvoiceStatus status,
                                        @Param("date") LocalDate date);
}