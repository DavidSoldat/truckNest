package com.trucknest.backend.dashboard;

import java.math.BigDecimal;

public record DashboardStats(
        long totalTrucks,
        long totalDrivers,
        long pendingInvoicesCount,
        BigDecimal pendingInvoicesTotal
) {}