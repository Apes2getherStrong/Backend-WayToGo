package com.example.waytogo.audio.model.dto;

import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.user.model.dto.UserDTO;
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
    String name;
/*    MultipartFile file;
    String filePath;*/
    UserDTO user;
    PointDTO point;
}
