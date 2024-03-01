package com.example.waytogo.user.mapper;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User userDtoToUser(UserDTO dto);

    UserDTO userToUserDto(User user);
    /*default User userDtoToUser(UserDTO dto) {
        if (dto == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.id(dto.getId());
            user.username(dto.getUsername());
            user.password(dto.getPassword());
            user.login(dto.getLogin());
            return user.build();
        }
    }

    default UserDTO userToUserDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserDTO.UserDTOBuilder userDTO = UserDTO.builder();
            userDTO.id(user.getId());
            userDTO.username(user.getUsername());
            userDTO.password(user.getPassword());
            userDTO.login(user.getLogin());
            return userDTO.build();
        }
    }*/
}
