package com.example.waytogo.user.controller;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController {
    public static final String USER_PATH = "/api/users";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

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

    @PostMapping(USER_PATH)
    public ResponseEntity<UserDTO> postUser(@RequestBody UserDTO userDTO) {
        UserDTO savedUser = userService.saveNewUser(userDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", USER_PATH + "/" + savedUser.getUserId().toString());

        return new ResponseEntity<>(savedUser, headers, HttpStatus.CREATED);
    }

    @PutMapping(USER_PATH_ID)
    public ResponseEntity<UserDTO> putUserById(@PathVariable("userId") UUID userId,
                                               @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserById(userId, userDTO);

        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @DeleteMapping(USER_PATH_ID)
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") UUID userId) {
            if (!userService.deleteUserById(userId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping(USER_PATH_ID)
    public ResponseEntity<Void> patchUserById(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        userService.patchUserById(userId, userDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
