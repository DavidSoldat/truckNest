package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.trucks.dto.TireResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TireMapper {

    @Mapping(source = "truck.id", target = "truckId")
    TireResponse toResponse(Tire tire);

    List<TireResponse> toResponseList(List<Tire> tires);
}