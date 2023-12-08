package com.example.waytogo.user.controller;

import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import com.example.waytogo.user.service.api.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class UserControllerIT {
    @Autowired
    UserController userController;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Rollback
    @Transactional
    @Test
    void testEmptyGetAllUsers() {
        userRepository.deleteAll();
        Page<UserDTO> dtos = userController.getAllUsers(1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void testGetAllUsers() {
        Page<UserDTO> dtos = userController.getAllUsers(1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(2);
    }

    @Test
    void testGetUserById() {
        User user = userRepository.findAll().get(0);

        ResponseEntity<UserDTO> dto = userController.getUserById(user.getId());

        assertThat(dto.getBody()).isNotNull();
    }

    @Test
    void testUserIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            userController.getUserById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("username")
                .login("login")
                .password("password")
                .build();

        ResponseEntity responseEntity = userController.postUser(userDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        System.out.println(responseEntity.getHeaders().getLocation().getPath().split("/")[4] );
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        User user = userRepository.findById(savedUUID).get();
        assertThat(user).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateExistingUser() {
        User user = userRepository.findAll().get(0);
        UserDTO userDTO = userMapper.userToUserDto(user);
        userDTO.setId(null);

        final String username = "UPDATED";
        userDTO.setUsername(username);

        ResponseEntity responseEntity = userController.putUserById(user.getId(), userDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo(username);
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        User user = userRepository.findAll().get(0);

        ResponseEntity responseEntity = userController.deleteUserById(user.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            userController.deleteUserById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchUserById() {
        User user = userRepository.findAll().get(0);
        UserDTO userDTO = userMapper.userToUserDto(user);

        final String username = "UPDATED";
        userDTO.setId(null);
        userDTO.setUsername(username);
        userDTO.setLogin(null);
        userDTO.setPassword(null);

        ResponseEntity responseEntity = userController.patchUserById(user.getId(), userDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo(username);
        assertThat(updatedUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    void testPatchUserByIdBadUsername() throws Exception {
        User user = userRepository.findAll().get(0);
        UserDTO userDTO = userMapper.userToUserDto(user);

        userDTO.setId(null);
        userDTO.setUsername("123456789012345678901234567890");

        /*Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", "New Name 123456789012345678901234567890");*/

        assertThrows(TransactionSystemException.class, () -> {
            userController.patchUserById(user.getId(), userDTO);
        });

        /*MvcResult result = mockMvc.perform(patch(UserController.USER_PATH_ID, user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());*/
    }


}