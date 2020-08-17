package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.PartDTO;
import com.googe.ssadm.sc.entity.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PartMapper {
    @Mapping(source = "supplier.id", target = "supplierId")
    PartDTO toPartDTO(Part part);

    List<PartDTO> toPartDTOs(List<Part> parts);

    Part toPart(PartDTO partDTO);
}
