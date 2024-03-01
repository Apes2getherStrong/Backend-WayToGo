package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.user.model.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AudioControllerIT {
    @Autowired
    AudioController audioController;

    @Autowired
    AudioService audioService;

    @Autowired
    AudioRepository audioRepository;

    @Autowired
    AudioMapper audioMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    GeometryFactory geometryFactory;


    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Rollback
    @Transactional
    @Test
    void testGetAllAudiosEmpty() {
        audioRepository.deleteAll();
        Page<AudioDTO> dtos = audioController.getAllAudios(1, 25).getBody();

        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Transactional
    @Test
    void testGetAllAudios() {
        Page<AudioDTO> dtos = audioController.getAllAudios(1, 25).getBody();

        assertThat(dtos.getContent().size()).isEqualTo(1);
    }

    @Transactional
    @Test
    void testGetAudioById() {
        Audio audio = audioRepository.findAll().get(0);

        ResponseEntity<AudioDTO> dto = audioController.getAudioById(audio.getId());

        assertThat(dto.getBody()).isNotNull();
        assertThat(dto.getBody().getName()).isEqualTo(audio.getName());
    }

    @Test
    void testGetAudioByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            audioController.getAudioById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewAudio() {
        AudioDTO audioDTO = getAudioDto();

        ResponseEntity<AudioDTO> responseEntity = audioController.postAudio(audioDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        System.out.println(responseEntity.getHeaders().getLocation().getPath().split("/")[4]);
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Audio audio = audioRepository.findById(savedUUID).get();
        assertThat(audio).isNotNull();
        assertThat(audio.getName()).isEqualTo(audioDTO.getName());
        assertThat(audio.getDescription()).isEqualTo(audioDTO.getDescription());
    }

    @Transactional
    @Test
    void testUpdateExistingAudio() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);
        audioDTO.setId(null);

        final String name = "UPDATED";
        audioDTO.setName(name);

        assertThat(audioDTO.getName()).isNotEqualTo(audio.getName());

        ResponseEntity<AudioDTO> responseEntity = audioController.putAudioById(audio.getId(), audioDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Audio updatedAudio = audioRepository.findById(audio.getId()).get();
        assertThat(updatedAudio.getName()).isEqualTo(name);
        assertThat(updatedAudio.getDescription()).isEqualTo(audio.getDescription());
    }

    @Test
    void testUpdateAudioNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            audioController.patchAudioById(UUID.randomUUID(), getAudioDto());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteAudioById() {
        Audio audio = audioRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = audioController.deleteAudioById(audio.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(audioRepository.findById(audio.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            audioController.deleteAudioById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchAudioById() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);
        audioDTO.setId(null);

        final String name = "UPDATED";
        audioDTO.setName(name);
        audioDTO.setDescription(null);
        audioDTO.setUser(null);
        audioDTO.setMapLocation(null);

        ResponseEntity<Void> responseEntity = audioController.patchAudioById(audio.getId(), audioDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Audio updatedAudio = audioRepository.findById(audio.getId()).get();
        assertThat(updatedAudio.getName()).isEqualTo(name);
        assertThat(updatedAudio.getDescription()).isEqualTo(audio.getDescription());
        assertThat(updatedAudio.getMapLocation()).isEqualTo(audio.getMapLocation());
        assertThat(updatedAudio.getUser()).isEqualTo(audio.getUser());
    }

    @Disabled
    @DisplayName("Nie dziala blad przy za duzym name")
    @Transactional
    @Test
    void testPatchUserByIdNameTooLong() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);

        audioDTO.setId(null);
        audioDTO.setName("namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename");

        assertThrows(TransactionSystemException.class, () -> {
            audioController.patchAudioById(audio.getId(), audioDTO);
        });
    }

    @Disabled
    @DisplayName("Nie dziala blad przy za duzym description")
    @Transactional
    @Test
    void testPatchUserByIdDescriptionTooLong() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);

        audioDTO.setId(null);
        audioDTO.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(audio.getDescription().length());

        assertThrows(TransactionSystemException.class, () -> {
            audioController.patchAudioById(audio.getId(), audioDTO);
        });
    }

    @Test
    void testPatchUserByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            audioController.patchAudioById(UUID.randomUUID(), getAudioDto());
        });
    }

    AudioDTO getAudioDto() {
        return AudioDTO.builder()
                .name("name")
                .description("desc")
                .user(UserDTO.builder()
                        .password("p")
                        .login("l")
                        .username("u")
                        .build())
                .mapLocation(MapLocationDTO.builder()
                        .coordinates(geometryFactory.createPoint(new Coordinate(30.1, 30.2)))
                        .name("n")
                        .build())
                .build();
    }
}



























