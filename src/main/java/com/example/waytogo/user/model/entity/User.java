package com.example.waytogo.user.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID userId;

    String username;

    String password;

    String login;

    @OneToMany(mappedBy = "user")
    List<Audio> audios;

    @OneToMany(mappedBy = "user")
    List<Route> routes;
}
