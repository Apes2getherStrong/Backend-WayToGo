package com.example.waytogo.routes_maplocation.entity;

import com.example.waytogo.maplocation.model.entity.MapLocation;
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
@Table(name = "routes_mapLocations")
public class RouteMapLocation {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "routes_map_location_id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name="map_location_id")
    private MapLocation mapLocation;

    @NotNull
    @ManyToOne
    @JoinColumn(name="route_id")
    private Route route;

    @NotNull
    @Min(0)
    @Max(1000)
    @Column(name = "sequence_nr")
    private Integer sequenceNr;
}
