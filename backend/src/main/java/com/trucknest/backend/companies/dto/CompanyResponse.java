package com.trucknest.backend.companies.dto;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String name,
        String contactEmail
) {}