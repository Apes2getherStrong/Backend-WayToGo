package com.example.waytogo.audio.model.entity;


import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Audio {
    @Id
    UUID uuid;

    @ManyToOne
    User user;
}
