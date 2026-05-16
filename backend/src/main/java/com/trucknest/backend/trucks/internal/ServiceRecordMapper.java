package com.trucknest.backend.trucks.internal;

import com.trucknest.backend.trucks.dto.ServiceRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceRecordMapper {

    @Mapping(source = "truck.id", target = "truckId")
    ServiceRecordResponse toResponse(ServiceRecord record);

    List<ServiceRecordResponse> toResponseList(List<ServiceRecord> records);
}