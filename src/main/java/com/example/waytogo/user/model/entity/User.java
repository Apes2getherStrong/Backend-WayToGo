package com.example.waytogo.user.model.entity;

import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name="routes")
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private UUID id;
    private String username;
    @OneToMany(mappedBy = "user")
    private List<Route> routes = new ArrayList<>();
}
