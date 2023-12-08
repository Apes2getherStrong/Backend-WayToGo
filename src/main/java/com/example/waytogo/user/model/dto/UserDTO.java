package com.example.waytogo.user.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    UUID userId;

    String username;

    String password;

    String login;
}
