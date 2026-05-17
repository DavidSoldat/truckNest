package com.trucknest.backend.dashboard;

import com.trucknest.backend.common.dto.DriverDocumentDto;
import com.trucknest.backend.common.dto.InvoiceOverdueDto;
import com.trucknest.backend.common.dto.TruckServiceDueDto;

import java.util.List;

public record DashboardResponse(
        List<TruckServiceDueDto> servicesDue,
        List<DriverDocumentDto> documentsExpiring,
        List<InvoiceOverdueDto> overdueInvoices,
        DashboardStats stats
) {}