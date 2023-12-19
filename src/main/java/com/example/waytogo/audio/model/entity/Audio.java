package com.example.waytogo.audio.model.entity;


import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audios")
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audio_id", updatable = false, nullable = false)
    UUID id;

    @NotNull
    @NotBlank
    @Size(max = 20)
    @Column(name = "audio_name", length = 20)
    String name;

    @Size(max = 255)
    @Column(length = 255)
    String description;

/*
    @Transient
    MultipartFile file;

    @Column(name = "file_path")
    String filePath;
*/

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_location_id")
    MapLocation mapLocation;
}
