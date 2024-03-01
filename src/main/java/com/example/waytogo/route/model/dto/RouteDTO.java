package com.example.waytogo.route.model.dto;

import com.example.waytogo.user.model.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    private UUID id;

    private UserDTO user;

    @NotBlank
    @NotNull
    @Size(max = 32)
    private String name;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

}
