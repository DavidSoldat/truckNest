package com.trucknest.backend.clients.internal;

import com.trucknest.backend.clients.dto.ClientResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponse toResponse(Client client);

    List<ClientResponse> toResponseList(List<Client> clients);
}