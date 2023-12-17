package com.example.waytogo.maplocation.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.locationtech.jts.geom.Coordinates;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "map_location")
public class MapLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "map_location_id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany(mappedBy="mapLocation")
    private List<Audio> audios;

    @OneToMany(mappedBy = "mapLocation")
    private List<RouteMapLocation> routes;

    @Column(name = "name")
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;


//    @Column(columnDefinition = "POINT")
//    private Point coordinates;


    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

}
