package com.trucknest.backend.trucks;

import com.trucknest.backend.common.dto.TruckServiceDueDto;
import com.trucknest.backend.trucks.internal.TruckRepository;
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
                        truck.getCompanyId()
                ))
                .toList();
    }

    public long countByCompanyId(UUID companyId) {
        return truckRepository.findAllByCompanyId(companyId).size();
    }
}