package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.ClientDTO;
import com.googe.ssadm.sc.entity.Client;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ClientMapper {
    ClientDTO toClientDTO(Client client);

    Client toClient(ClientDTO clientDTO);
}
