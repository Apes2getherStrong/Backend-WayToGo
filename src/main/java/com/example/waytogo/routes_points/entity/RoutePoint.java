package com.example.waytogo.routes_points.entity;

import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.route.model.entity.Route;
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
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "routes_points_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="point_id")
    private Point point;

    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;

    @Column(name = "sequence_nr")
    private Integer sequenceNr;
}
