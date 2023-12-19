package com.example.waytogo.audio.model.dto;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.user.model.dto.UserDTO;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
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
public class AudioDTO {
    UUID id;

    @NotNull
    @NotBlank
    @Valid
    @Size(max = 20)
    @Column(length = 20)
    String name;

    @Size(max = 255)
    @Column(length = 255)
    String description;
/*    MultipartFile file;
    String filePath;*/
    UserDTO user;
    MapLocationDTO mapLocation;
}
