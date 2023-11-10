package com.example.waytogo.user.service.api;

import com.example.waytogo.user.model.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Page<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize);

    Optional<UserDTO> getUserById(UUID userId);
}
