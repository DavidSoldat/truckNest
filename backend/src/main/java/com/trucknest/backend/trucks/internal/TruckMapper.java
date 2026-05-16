package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.trucks.dto.TruckResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    TruckResponse toResponse(Truck truck);

    List<TruckResponse> toResponseList(List<Truck> trucks);
}