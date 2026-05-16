package com.trucknest.backend.clients.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findAllByCompanyId(UUID companyId);

    Optional<Client> findByIdAndCompanyId(UUID id, UUID companyId);
}
