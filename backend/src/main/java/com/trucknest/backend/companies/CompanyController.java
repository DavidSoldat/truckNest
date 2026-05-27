package com.trucknest.backend.companies;

import com.trucknest.backend.common.entity.Company;
import com.trucknest.backend.common.entity.CompanyRepository;
import com.trucknest.backend.common.tenant.TenantContext;
import com.trucknest.backend.companies.dto.CompanyResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyResponse> getMyCompany() {
        UUID companyId = UUID.fromString(TenantContext.getTenantId());
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return ResponseEntity.ok(new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getContactEmail()
        ));
    }
}