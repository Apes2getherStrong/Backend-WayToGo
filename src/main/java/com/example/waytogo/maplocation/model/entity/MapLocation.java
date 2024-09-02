package com.example.waytogo.maplocation.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.maplocation.model.entity.coordinateTools.ValidCoordinates;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "map_locations")
public class MapLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "map_location_id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "mapLocation", fetch = FetchType.LAZY)
    private List<Audio> audios;

    @OneToMany(mappedBy = "mapLocation", fetch = FetchType.LAZY)
    private List<RouteMapLocation> routes;

    @Column(name = "name")
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    @Column(columnDefinition = "Geometry(point)")
    @NotNull
    @ValidCoordinates
    private Point coordinates;

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;


}
