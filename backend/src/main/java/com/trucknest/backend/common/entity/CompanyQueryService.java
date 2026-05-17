package com.trucknest.backend.common.entity;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyQueryService {

    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public String getCompanyEmail(UUID companyId) {
        return companyRepository.findById(companyId)
                .map(Company::getContactEmail)
                .orElse(null);
    }
}