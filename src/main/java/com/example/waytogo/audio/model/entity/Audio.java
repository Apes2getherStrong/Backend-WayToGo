package com.example.waytogo.audio.model.entity;


import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Audio {
    @Id
    UUID uuid;

    @ManyToOne
    User user;
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="point_id")
    Point point;
}
