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
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.*;

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
        Page<AudioDTO> dtos = audioController.getAllAudios(1,25);

        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void testGetAllAudios() {
        Page<AudioDTO> dtos = audioController.getAllAudios(1,25);

        assertThat(dtos.getContent().size()).isEqualTo(1);
    }

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
        System.out.println(responseEntity.getHeaders().getLocation().getPath().split("/")[4] );
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Audio audio = audioRepository.findById(savedUUID).get();
        assertThat(audio).isNotNull();
    }

    @Test
    void testUpdateExistingAudio() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);
        audioDTO.setId(null);

        final String name = "UPDATED";
        audioDTO.setName(name);

        ResponseEntity<AudioDTO> responseEntity = audioController.putAudioById(audio.getId(), audioDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Audio updatedAudio = audioRepository.findById(audio.getId()).get();
        assertThat(updatedAudio.getName()).isEqualTo(name);
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
        audioDTO.setUser(null);
        audioDTO.setMapLocationDTO(null);

        ResponseEntity<Void> responseEntity = audioController.patchAudioById(audio.getId(), audioDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Audio updatedAudio = audioRepository.findById(audio.getId()).get();
        assertThat(updatedAudio.getName()).isEqualTo(name);
        assertThat(updatedAudio.getMapLocation()).isEqualTo(audio.getMapLocation());
        assertThat(updatedAudio.getUser()).isEqualTo(audio.getUser());
    }

    @Test
    void testPatchUserByIdBadName() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);

        audioDTO.setId(null);
        audioDTO.setName("namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename");

        assertThrows(TransactionSystemException.class, () -> {
            audioController.patchAudioById(audio.getId(), audioDTO);
        });
    }

    AudioDTO getAudioDto() {
        return AudioDTO.builder()
                .name("name")
                .user(UserDTO.builder()
                        .password("p")
                        .login("l")
                        .username("u")
                        .build())
                .mapLocationDTO(MapLocationDTO.builder()
                        .name("n")
                        .build())
                .build();
    }
}



























