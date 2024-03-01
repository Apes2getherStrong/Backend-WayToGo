package com.example.waytogo.audio.model.entity;


import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Size(max = 100)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    //^ for some reason set null is not working.
    //Instead, appropriate method in service was implemented
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_location_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    MapLocation mapLocation;
}
