package com.example.waytogo.user.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.util.RouteMatcher;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID userId;

    String username;

    String password;

    String login;

    @OneToMany
    @ToString.Exclude
    List<Audio> audios;

    @OneToMany
    @ToString.Exclude
    List<Route> routes;
}
