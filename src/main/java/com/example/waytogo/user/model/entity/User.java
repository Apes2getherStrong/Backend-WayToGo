package com.example.waytogo.user.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false, nullable = false)
    UUID id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50, unique = true)
    String username;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Column(length = 100)
    String password;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    String login;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Audio> audios;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Route> routes;
}
