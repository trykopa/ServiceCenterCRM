package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.MessageDTO;
import com.googe.ssadm.sc.entity.Message;
import com.googe.ssadm.sc.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = { UserService.class })
@Component
public interface MessageMapper {
    @Mapping(source = "fromUser.id", target = "fromUser")
    @Mapping(source = "toUser.id", target = "toUser")
    MessageDTO toMessageDTO(Message message);

    @Mapping(source = "fromUser", target = "fromUser.id")
    @Mapping(source = "toUser", target = "toUser.id")
    Message toMessage(MessageDTO messageDTO);
}
