package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.OrderDTO;
import com.googe.ssadm.sc.entity.Client;
import com.googe.ssadm.sc.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface OrderMapper {

    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "client.surname", target = "clientSurname")
    @Mapping(source = "client.mobile", target = "clientMobile")
    @Mapping(source = "client.email", target = "clientEmail")
    @Mapping(source = "order.id", target = "id")
    @Mapping(source = "order.status", target = "status")
    @Mapping(source = "order.createdAt", target = "createdDate")
    OrderDTO toOrderDto(Order order, Client client);

    default List<OrderDTO> toOrdersDTOs(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderDTO> list = new ArrayList<OrderDTO>( orders.size() );
        for ( Order order : orders ) {
            list.add( toOrderDto( order, order.getClient()) );
        }

        return list;
    }

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(OrderDTO orderDTO);
}
