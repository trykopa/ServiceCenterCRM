package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.SupplierDTO;
import com.googe.ssadm.sc.entity.Supplier;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface SupplierMapper {
    SupplierDTO toSupplierDto(Supplier supplier);
    List<SupplierDTO> toSupplierDTOs(List<Supplier> suppliers);

    Supplier toSupplier(SupplierDTO supplierDTO);
}
