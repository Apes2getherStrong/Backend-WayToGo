package com.example.waytogo.audio.mapper;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AudioMapperTest {
    @Autowired
    AudioMapper audioMapper;

    @Autowired
    GeometryFactory geometryFactory;

    Audio audio;
    AudioDTO audioDTO;

    User user;
    UserDTO userDTO;

    MapLocation mapLocation;
    MapLocationDTO mapLocationDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.fromString("a0769a1e-5f77-487c-9861-30af08f74147"))
                .password("p")
                .login("l")
                .username("u")
                .build();

        userDTO = UserDTO.builder()
                .id(UUID.fromString("a0769a1e-5f77-487c-9861-30af08f74147"))
                .password("p")
                .login("l")
                .username("u")
                .build();

        mapLocation = MapLocation.builder()
                .id(UUID.fromString("6cfc986f-3c82-41ef-bcb6-ff7936e9f600"))
                .name("n")
                .description("d")
                .coordinates(geometryFactory.createPoint(new Coordinate(5.2, 11.33)))
                .build();

        mapLocationDTO = MapLocationDTO.builder()
                .id(UUID.fromString("6cfc986f-3c82-41ef-bcb6-ff7936e9f600"))
                .name("n")
                .description("d")
                .coordinates(geometryFactory.createPoint(new Coordinate(5.2, 11.33)))
                .build();

        audio = Audio.builder()
                .id(UUID.fromString("ef1e7094-f4ba-48de-9a1f-746958b7a5c6"))
                .name("name")
                .description("description")
                .user(user)
                .mapLocation(mapLocation)
                .build();

        audioDTO = AudioDTO.builder()
                .id(UUID.fromString("ef1e7094-f4ba-48de-9a1f-746958b7a5c6"))
                .name("name")
                .description("description")
                .user(userDTO)
                .mapLocation(mapLocationDTO)
                .build();
    }

    @Test
    void testAudioToAudioDto() {
        AudioDTO mapped = audioMapper.audioToAudioDto(audio);

        assertThat(mapped.getId()).isEqualTo(audio.getId());
        assertThat(mapped.getName()).isEqualTo(audio.getName());
        assertThat(mapped.getDescription()).isEqualTo(audio.getDescription());

        assertThat(mapped.getUser().getId()).isEqualTo(audio.getUser().getId());
        assertThat(mapped.getUser().getLogin()).isEqualTo(audio.getUser().getLogin());
        assertThat(mapped.getUser().getPassword()).isEqualTo(audio.getUser().getPassword());
        assertThat(mapped.getUser().getUsername()).isEqualTo(audio.getUser().getUsername());


        assertThat(mapped.getMapLocation().getId()).isEqualTo(audio.getMapLocation().getId());
        assertThat(mapped.getMapLocation().getName()).isEqualTo(audio.getMapLocation().getName());
        assertThat(mapped.getMapLocation().getDescription()).isEqualTo(audio.getMapLocation().getDescription());
        assertThat(mapped.getMapLocation().getCoordinates()).isEqualTo(audio.getMapLocation().getCoordinates());
    }

    @Test
    void testAudioDtoToAudio() {
        Audio mapped = audioMapper.audioDtoToAudio(audioDTO);

        assertThat(mapped.getId()).isEqualTo(audioDTO.getId());
        assertThat(mapped.getName()).isEqualTo(audioDTO.getName());
        assertThat(mapped.getDescription()).isEqualTo(audioDTO.getDescription());

        assertThat(mapped.getUser().getId()).isEqualTo(audioDTO.getUser().getId());
        assertThat(mapped.getUser().getLogin()).isEqualTo(audioDTO.getUser().getLogin());
        assertThat(mapped.getUser().getPassword()).isEqualTo(audioDTO.getUser().getPassword());
        assertThat(mapped.getUser().getUsername()).isEqualTo(audioDTO.getUser().getUsername());


        assertThat(mapped.getMapLocation().getId()).isEqualTo(audioDTO.getMapLocation().getId());
        assertThat(mapped.getMapLocation().getName()).isEqualTo(audioDTO.getMapLocation().getName());
        assertThat(mapped.getMapLocation().getDescription()).isEqualTo(audioDTO.getMapLocation().getDescription());
        assertThat(mapped.getMapLocation().getCoordinates()).isEqualTo(audioDTO.getMapLocation().getCoordinates());
    }
}