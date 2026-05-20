package com.trucknest.backend.registration.dto;

import java.util.UUID;

public record RegisterResponse(
        UUID companyId,
        String message
) {}