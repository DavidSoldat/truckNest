package com.trucknest.backend.registration.internal;

import com.trucknest.backend.common.entity.Company;
import com.trucknest.backend.common.entity.CompanyRepository;
import com.trucknest.backend.registration.dto.RegisterRequest;
import com.trucknest.backend.registration.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RegistrationService {

    private final CompanyRepository companyRepository;
    private final KeycloakAdminService keycloakAdminService;

    public RegistrationService(CompanyRepository companyRepository, KeycloakAdminService keycloakAdminService) {
        this.companyRepository = companyRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (companyRepository.existsByContactEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Company company = new Company();
        company.setName(request.companyName());
        company.setContactEmail(request.email());
        company.setCompanyId(java.util.UUID.randomUUID());
        Company saved = companyRepository.save(company);

        saved.setCompanyId(saved.getId());
        saved = companyRepository.save(saved);

        String keycloakUserId = null;
        try {
            keycloakUserId = keycloakAdminService.createUser(
                    request.ownerFirstName(),
                    request.ownerLastName(),
                    request.email(),
                    request.password(),
                    saved.getId()
            );
        } catch (Exception e) {
            log.error("Keycloak user creation failed, rolling back company record", e);
            companyRepository.delete(saved);
            throw new RuntimeException("Registration failed: could not create user account");
        }

        log.info("Registered company {} with Keycloak user {}", saved.getId(), keycloakUserId);
        return new RegisterResponse(saved.getId(), "Registration successful");
    }
}