package com.example.waytogo.user.service.impl;

import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceJPA userService;

    @Autowired
    UserMapper userMapper;

    User user;
    User userNoLogin;

    UserDTO userDTO;
    UserDTO userDTONoLogin;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .login("login")
                .password("password")
                .username("username")
                .build();
        userNoLogin = User.builder()
                .password("password2")
                .username("username2")
                .build();

        userDTO = userMapper.userToUserDto(user);
        userDTONoLogin = userMapper.userToUserDto(userNoLogin);
    }

    @Test
    void testGetAllUsers() {
        Page<UserDTO> users = userService.getAllUsers(1, 25);

        assertThat(users.getSize()).isEqualTo(25);
    }

    @Rollback
    @Transactional
    @Test
    void testGetUserById() {
        User savedUser = userRepository.save(user);

        UserDTO foundDTO = userService.getUserById(savedUser.getId()).get();

        assertEquals(savedUser.getId(), foundDTO.getId());
        assertEquals(savedUser.getUsername(), foundDTO.getUsername());
        assertEquals(savedUser.getPassword(), foundDTO.getPassword());
        assertEquals(savedUser.getLogin(), foundDTO.getLogin());
    }

    @Test
    void testGetUserByIdNotFound() {
        Optional<UserDTO> dto = userService.getUserById(UUID.randomUUID());

        assertThat(dto).isEmpty();
    }

    @Transactional
    @Rollback
    @Test
    void testSaveUser() {
        UserDTO savedUser = userService.saveNewUser(userDTO);

        assertThat(savedUser).isNotNull();
        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Transactional
    @Rollback
    @Test
    void testSaveUserNotValid() {
        assertThrows(ConstraintViolationException.class, () -> {
            userService.saveNewUser(UserDTO.builder().build());
        });

        //No username
        assertThrows(ConstraintViolationException.class, () -> {
            userService.saveNewUser(UserDTO.builder()
                    .login("l")
                    .password("p")
                    .build());
        });

        //No password
        assertThrows(ConstraintViolationException.class, () -> {
            userService.saveNewUser(UserDTO.builder()
                    .login("l")
                    .username("u")
                    .build());
        });

        //No login
        assertThrows(ConstraintViolationException.class, () -> {
            userService.saveNewUser(UserDTO.builder()
                    .username("u")
                    .password("p")
                    .build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateUserById() {
        UserDTO dto = userMapper.userToUserDto(userRepository.findAll().get(0));
        dto.setUsername("UPDATED");

        UserDTO saved = userService.updateUserById(dto.getId(), dto).get();

        //Wszytkie 3 asserty to to samo, ale nie wiem ktory najlepiej uzywac
        //assertEquals(userService.getUserById(dto.getId()).get().getUsername(), "UPDATED");
        //assertEquals(saved.getUsername(), "UPDATED");
        assertEquals(saved.getUsername(), dto.getUsername());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteUserById() {
        User saved = userRepository.save(user);

        assertThat(userService.getUserById(saved.getId())).isNotEmpty();

        assertTrue(userService.deleteUserById(saved.getId()));

        assertThat(userService.getUserById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteUserByIdNotExist() {
        assertFalse(userService.deleteUserById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchUserById() {
        User saved = userRepository.save(user);

        UserDTO updated = userService.patchUserById(saved.getId(), userDTONoLogin).get();

        assertThat(updated).isNotNull();
        assertThat(updated.getUsername()).isEqualTo(userDTONoLogin.getUsername());
        assertThat(updated.getPassword()).isEqualTo(userDTONoLogin.getPassword());

        assertThat(updated.getLogin()).isEqualTo(userDTO.getLogin());


    }

    @Test
    void testPatchUserByIdBadId() {
        assertThat(userService.patchUserById(UUID.randomUUID(), userDTO)).isEmpty();
    }
}