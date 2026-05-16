package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.drivers.dto.IncidentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    @Mapping(source = "driver.id", target = "driverId")
    IncidentResponse toResponse(Incident incident);

    List<IncidentResponse> toResponseList(List<Incident> incidents);
}