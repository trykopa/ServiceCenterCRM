package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.UserDTO;
import com.googe.ssadm.sc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserDTO toUserDTO(User user);
    List<UserDTO> toUserDTOs(List<User> users);
    @Mapping(source = "roles", target = "roles")
    User toUser(UserDTO userDTO);
}
