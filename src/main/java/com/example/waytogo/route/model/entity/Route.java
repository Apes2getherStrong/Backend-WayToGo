package com.example.waytogo.route.model.entity;

import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name="routes")
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="route_id", updatable=false, nullable=false)
    private UUID id;

    @ManyToOne
    private User user;

    private String name;
}
