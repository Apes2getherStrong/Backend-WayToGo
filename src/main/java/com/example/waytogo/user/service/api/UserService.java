package com.example.waytogo.user.service.api;

import com.example.waytogo.user.model.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
public interface UserService {
    Page<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize);

    Optional<UserDTO> getUserById(UUID userId);

    UserDTO saveNewUser(@Valid UserDTO userDTO);

    Optional<UserDTO> updateUserById(UUID userId, @Valid UserDTO userDTO);

    boolean deleteUserById(UUID userId);

    Optional<UserDTO> patchUserById(UUID userId, UserDTO userDTO);

    boolean existsByUsername(String username);
}
