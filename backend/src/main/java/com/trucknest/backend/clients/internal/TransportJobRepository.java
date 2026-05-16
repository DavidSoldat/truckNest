package com.trucknest.backend.clients.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransportJobRepository extends JpaRepository<TransportJob, UUID> {

    List<TransportJob> findAllByClientIdAndCompanyIdOrderByJobDateDesc(UUID clientId, UUID companyId);

    List<TransportJob> findAllByCompanyIdOrderByJobDateDesc(UUID companyId);
}