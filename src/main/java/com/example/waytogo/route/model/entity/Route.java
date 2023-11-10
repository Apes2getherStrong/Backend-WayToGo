package com.example.waytogo.route.model.entity;

import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Route {
    @Id
    UUID uuid;

    @ManyToOne
    User user;
}
