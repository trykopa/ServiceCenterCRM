package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.RepairTableDTO;
import com.googe.ssadm.sc.entity.RepairTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RepairTableMapper {
    @Mapping(source = "partList", target = "partDTOList")
    @Mapping(source = "order.id", target = "orderId")
    RepairTableDTO repTableToRepTableDTO(RepairTable repairTable);

    RepairTable repTableDtotoRepTable(RepairTableDTO repairTableDTO);
}
