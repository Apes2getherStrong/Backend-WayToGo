package com.example.waytogo.route.model.entity;

import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name="routes")
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="route_id", updatable=false, nullable=false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    //^ for some reason set null is not working.
    //Instead, appropriate method in service was implemented
    private User user;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<RouteMapLocation> routeMapLocations;

    @NotBlank
    @NotNull
    @Size(max=32)
    private String name;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    /*
    //Potrzebne do zdjecia
    @Transient
    MultipartFile file;

    @Column(name = "file_path")
    String filePath;
    */
}
