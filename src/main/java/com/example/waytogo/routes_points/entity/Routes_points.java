package com.example.waytogo.routes_points.entity;

import com.example.waytogo.point.model.entity.Point;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "routes_points")
public class Routes_points {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "routes_points_id", updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name="point_id")
    private Point point;
}
