package com.example.waytogo.routes_maplocation.model.entity;

import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.entity.Route;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_location_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MapLocation mapLocation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Route route;

    @NotNull
    @Min(0)
    @Max(1000)
    @Column(name = "sequence_nr")
    private Integer sequenceNr;
}
