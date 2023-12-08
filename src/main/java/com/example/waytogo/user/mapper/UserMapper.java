package com.example.waytogo.user.mapper;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDtoToUser(UserDTO dto);

    UserDTO userToUserDto(User user);
}
