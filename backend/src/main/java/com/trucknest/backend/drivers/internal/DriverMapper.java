package com.trucknest.backend.drivers.internal;

import com.trucknest.backend.drivers.dto.DriverResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverResponse toResponse(Driver driver);

    List<DriverResponse> toResponseList(List<Driver> drivers);
}