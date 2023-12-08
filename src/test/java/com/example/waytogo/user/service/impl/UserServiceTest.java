package com.example.waytogo.user.service.impl;

import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.mapper.UserMapperImpl;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceJPA userService;

    @Autowired
    UserMapper userMapper;

    @Transactional
    @Rollback
    @Test
    void testSaveUser() {
        User user = User.builder()
                .password("haslo")
                .login("login")
                .username("username")
                .build();

        UserDTO userDTO = userMapper.userToUserDto(user);

        UserDTO savedUser = userService.saveNewUser(userDTO);

        assertThat(savedUser).isNotNull();
    }

    @Test
    void testSave() {
    }
}