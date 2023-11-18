package com.example.waytogo.audio.model.entity;


import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    UUID uuid;

/*
    @Transient
    MultipartFile file;

    @Column(name = "file_path")
    String filePath;
*/

    @ManyToOne
    User user;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="point_id")
    Point point;
}
