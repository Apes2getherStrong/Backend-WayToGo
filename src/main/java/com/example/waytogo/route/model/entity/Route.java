package com.example.waytogo.route.model.entity;

import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name="routes")
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="route_id", updatable=false, nullable=false)
    private UUID id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "route", cascade = CascadeType.REMOVE)
    private List<RoutePoint> routePoints;

    @NotBlank
    @NotNull
    @Size(max=32)
    private String name;
}
