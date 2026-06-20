package com.trucknest.backend.trucks;

import com.trucknest.backend.common.dto.TruckServiceDueDto;
import com.trucknest.backend.trucks.internal.TruckRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TruckQueryService {

    private final TruckRepository truckRepository;

    public TruckQueryService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public List<TruckServiceDueDto> findTrucksWithServiceDueBefore(LocalDate date) {
        return truckRepository.findAllByNextServiceDateBefore(date)
                .stream()
                .map(truck -> new TruckServiceDueDto(
                        truck.getId(),
                        truck.getPlateNumber(),
                        truck.getNextServiceDate(),
                        truck.getServiceDueNotifiedFor(),
                        truck.getCompanyId()
                ))
                .toList();
    }

    public long countByCompanyId(UUID companyId) {
        return truckRepository.findAllByCompanyId(companyId).size();
    }

    @Transactional
    public void markServiceDueNotified(UUID truckId, LocalDate serviceDate) {
        truckRepository.markServiceDueNotified(truckId, serviceDate);
    }
}