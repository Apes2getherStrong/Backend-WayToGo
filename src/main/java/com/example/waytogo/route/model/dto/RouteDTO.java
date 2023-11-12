package com.example.waytogo.route.model.dto;

import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private String name;
}
