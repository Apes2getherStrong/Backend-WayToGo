package com.example.waytogo.user.controller;

import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.mapper.UserMapperImpl;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.service.api.UserService;
import com.example.waytogo.user.service.impl.UserServiceJPA;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
//@Import(UserMapper.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    //@Autowired
    UserMapper userMapper;

    User user1;
    User user2;
    User user3;
    User userNoLogin;
    User userNoPassword;
    User userNoUsername;

    UserDTO userDTO1;
    UserDTO userDTO2;
    UserDTO userDTO3;
    UserDTO userNoLoginDTO;
    UserDTO userNoPasswordDTO;
    UserDTO userNoUsernameDTO;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
        user1 = User.builder()
                .userId(UUID.randomUUID())
                .login("login1")
                .username("username1")
                .password("password1")
                .build();
        user2 = User.builder()
                .userId(UUID.randomUUID())
                .login("login2")
                .username("username2")
                .password("password2")
                .build();
        user3 = User.builder()
                .userId(UUID.randomUUID())
                .login("login3")
                .username("username3")
                .password("password3")
                .build();
        userNoLogin = User.builder()
                .userId(UUID.randomUUID())
                .username("username")
                .password("password")
                .build();
        userNoPassword = User.builder()
                .userId(UUID.randomUUID())
                .login("login")
                .username("username")
                .build();
        userNoUsername = User.builder()
                .userId(UUID.randomUUID())
                .login("login")
                .password("password")
                .build();

        userDTO1 = userMapper.userToUserDto(user1);
        userDTO2 = userMapper.userToUserDto(user2);
        userDTO3 = userMapper.userToUserDto(user3);
        userNoLoginDTO = userMapper.userToUserDto(userNoLogin);
        userNoPasswordDTO = userMapper.userToUserDto(userNoLogin);
        userNoUsernameDTO = userMapper.userToUserDto(userNoUsername);
    }

    @Test
    void testCreateUser() throws Exception {
        //given(userService.saveNewUser(any())).willAnswer((invocation -> invocation.getArgument(0)));
        given(userService.saveNewUser(any())).willReturn(userDTO1);

        mockMvc.perform(post(UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


}
































