package com.example.waytogo.route.model.dto;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutRouteRequest {
    private UUID userId;
    private String name;
}
