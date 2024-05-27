package com.example.waytogo.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {
    UUID id;

    @NotNull
    @NotBlank
    @Valid
    @Size(max = 20)
    @Column(length = 20)
    @JsonProperty("username")
    String username;

    @NotNull
    @NotBlank
    @Valid
    @Size(max = 100)
    @Column(length = 100)
    @JsonProperty("password")
    String password;

    @NotNull
    @NotBlank
    @Valid
    @Size(max = 50)
    @Column(length = 50)
    @JsonProperty("login")
    String login;
}
