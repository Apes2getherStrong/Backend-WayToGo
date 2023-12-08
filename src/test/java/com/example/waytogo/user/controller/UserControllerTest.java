package com.example.waytogo.user.controller;

import com.example.waytogo.point.model.dto.PointDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<UserDTO> userDTOArgumentCaptor;

    //@Autowired
    UserMapper userMapper;

    User user1;
    User user2;

    UserDTO userDTO1;
    UserDTO userDTO2;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
        user1 = User.builder()
                .id(UUID.randomUUID())
                .login("login1")
                .username("username1")
                .password("password1")
                .build();
        user2 = User.builder()
                .id(UUID.randomUUID())
                .login("login2")
                .username("username2")
                .password("password2")
                .build();

        userDTO1 = userMapper.userToUserDto(user1);
        userDTO2 = userMapper.userToUserDto(user2);
    }

    @Test
    void testGetAllUsers() throws Exception {
        given(userService.getAllUsers(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(userDTO1, userDTO2))));

        mockMvc.perform(get(UserController.USER_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    void testGetUserById() throws Exception {
        given(userService.getUserById(any())).willReturn(Optional.of(userDTO1));

        mockMvc.perform(get(UserController.USER_PATH_ID, userDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDTO1.getId().toString())))
                .andExpect(jsonPath("$.username", is(userDTO1.getUsername())))
                .andExpect(jsonPath("$.login", is(userDTO1.getLogin())))
                .andExpect(jsonPath("$.password", is(userDTO1.getPassword())));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        given(userService.getUserById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(UserController.USER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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

    @Test
    void testCreateUserNullLoginNullPasswordNullUsername() throws Exception {
        UserDTO userDTOooo = UserDTO.builder().build();

        given(userService.saveNewUser(any(UserDTO.class))).willReturn(userDTO1);

        MvcResult mvcResult = mockMvc.perform(post(UserController.USER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOooo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPutUserById() throws Exception {
        given(userService.updateUserById(any(), any())).willReturn(userDTO1);

        mockMvc.perform(put(UserController.USER_PATH_ID, userDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO1)))
                .andExpect(status().isNoContent());
        verify(userService).updateUserById(any(UUID.class), any(UserDTO.class));
    }

    @Test
    void testDeleteUserById() throws Exception {
        given(userService.deleteUserById(any())).willReturn(true);

        mockMvc.perform(delete(UserController.USER_PATH_ID, userDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(userDTO1.getId());
    }

    @Test
    void testDeleteUserByIdNotFound() throws Exception {
        given(userService.deleteUserById(any())).willReturn(false);

        mockMvc.perform(delete(UserController.USER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchUserById() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "New username");

        given(userService.patchUserById(any(), any())).willReturn(Optional.of(userDTO1));

        mockMvc.perform(patch(UserController.USER_PATH_ID, userDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMap)))
                .andExpect(status().isNoContent());

        verify(userService).patchUserById(uuidArgumentCaptor.capture(), userDTOArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(userDTO1.getId());
        assertThat(userDTOArgumentCaptor.getValue().getUsername()).isEqualTo(userMap.get("username"));
    }

    @Test
    void testPatchUserByIdNotFound() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "New username");

        given(userService.patchUserById(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(patch(UserController.USER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMap)))
                .andExpect(status().isNotFound());
    }
}
































