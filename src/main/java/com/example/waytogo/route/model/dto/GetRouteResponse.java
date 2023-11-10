package com.example.waytogo.route.model.dto;

import com.example.waytogo.user.model.dto.UserDTO;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetRouteResponse {
    private UUID id;
    private UserDTO user;
    private String name;
}
