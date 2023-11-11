package com.example.waytogo.point.model.entity;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_points.entity.Routes_points;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id", updatable = false, nullable = false)
    private UUID uuid;

    @OneToMany(mappedBy="point")
    private List<Audio> audios;

    @OneToMany(mappedBy = "point")
    private List<Routes_points> routes;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "point", cascade = CascadeType.ALL)
    private Coordinates coordinates;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

}
