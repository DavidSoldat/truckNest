package com.trucknest.backend.common.exception;

public record ErrorResponse(
        String code,
        String message
) {}