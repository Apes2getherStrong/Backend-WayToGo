package com.example.waytogo.routes_points.entity;

import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "routes_points_id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="point_id")
    private Point point;

    @NotNull
    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "sequence_nr")
    private Integer sequenceNr;
}
