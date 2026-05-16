package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.drivers.dto.KmLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KmLogMapper {

    @Mapping(source = "driver.id", target = "driverId")
    KmLogResponse toResponse(KmLog kmLog);

    List<KmLogResponse> toResponseList(List<KmLog> kmLogs);
}