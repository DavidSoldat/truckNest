package com.trucknest.backend.trucks;

import com.trucknest.backend.trucks.dto.TruckServiceDueDto;
import com.trucknest.backend.trucks.internal.TruckRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
}