package com.example.waytogo.user.controller;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController {
    private static final String USER_PATH = "/api/users";
    private static final String USER_PATH_ID = USER_PATH + "/{userId}";

    private final UserService userService;

    @GetMapping(USER_PATH)
    public Page<UserDTO> getAllUsers(@RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer pageSize) {
        return userService.getAllUsers(pageNumber, pageSize);
    }

    @GetMapping(USER_PATH_ID)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
