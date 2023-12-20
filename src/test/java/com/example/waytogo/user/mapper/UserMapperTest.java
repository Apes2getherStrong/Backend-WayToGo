package com.example.waytogo.user.mapper;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.fromString("a0769a1e-5f77-487c-9861-30af08f74147"))
                .password("p")
                .login("l")
                .username("u")
                .build();

        userDTO = UserDTO.builder()
                .id(UUID.fromString("a0769a1e-5f77-487c-9861-30af08f74147"))
                .password("p")
                .login("l")
                .username("u")
                .build();
    }

    @Test
    void testUserToUserDto() {
        UserDTO mapped = userMapper.userToUserDto(user);

        assertEquals(mapped.getId(), user.getId());
        assertEquals(mapped.getUsername(), user.getUsername());
        assertEquals(mapped.getLogin(), user.getLogin());
        assertEquals(mapped.getPassword(), user.getPassword());
    }

    @Test
    void testUserDtoToUser() {
        User mapped = userMapper.userDtoToUser(userDTO);

        assertEquals(mapped.getId(), userDTO.getId());
        assertEquals(mapped.getUsername(), userDTO.getUsername());
        assertEquals(mapped.getLogin(), userDTO.getLogin());
        assertEquals(mapped.getPassword(), userDTO.getPassword());
    }
}