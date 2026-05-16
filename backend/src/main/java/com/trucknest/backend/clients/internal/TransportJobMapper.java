package com.trucknest.backend.clients.internal;

import com.trucknest.backend.clients.dto.TransportJobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransportJobMapper {

    @Mapping(source = "client.id", target = "clientId")
    TransportJobResponse toResponse(TransportJob job);

    List<TransportJobResponse> toResponseList(List<TransportJob> jobs);
}