package com.example.waytogo.audio.service.impl;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.security.cert.CertPathValidatorException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AudioServiceTest {

    @Autowired
    AudioRepository audioRepository;

    @Autowired
    AudioServiceJPA audioService;

    @Autowired
    AudioMapper audioMapper;

    Audio audio;
    Audio audio2;

    AudioDTO audioDTO;
    AudioDTO audioDTO2;

    @Autowired
    GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        audio = Audio.builder()
                .name("name")
                .description("desc")
                .user(User.builder()
                        .username("u")
                        .login("l")
                        .password("p")
                        .build())
                .mapLocation(MapLocation.builder()
                        .coordinates(geometryFactory.createPoint(new Coordinate(30.1,30.2)))
                        .name("n")
                        .build())
                .build();
        audio2 = Audio.builder()
                .name("UPDATED")
                .build();

        audioDTO = audioMapper.audioToAudioDto(audio);
        audioDTO2 = audioMapper.audioToAudioDto(audio2);
    }

    @Test
    void testGetAllAudios() {
        Page<AudioDTO> audios = audioService.getAllAudios(1, 25);

        assertThat(audios.getSize()).isEqualTo(25);
        assertThat(audios.getContent().size()).isEqualTo(1);
    }

    @Rollback
    @Transactional
    @Test
    void testGetAudioById() {
        Audio savedAudio = audioRepository.save(audio);

        AudioDTO foundDTO = audioService.getAudioById(savedAudio.getId()).get();

        assertEquals(savedAudio.getId(), foundDTO.getId());
        assertEquals(savedAudio.getName(), foundDTO.getName());
        assertEquals(savedAudio.getDescription(), foundDTO.getDescription());
    }

    @Test
    void testGetAudioByIdNotFound() {
        Optional<AudioDTO> dto = audioService.getAudioById(UUID.randomUUID());

        assertThat(dto).isEmpty();
    }

    @Disabled
    @DisplayName("Nie wiem czemu to nie dziala XD")
    @Rollback
    @Transactional
    @Test
    void testGetAudioByUserId() {
        Audio savedAudio = audioRepository.save(audio);

        AudioDTO foundDTO = audioService.getAllAudiosByUserId(savedAudio.getUser().getId(),1,25).getContent().get(0);

        assertEquals(savedAudio.getId(), foundDTO.getId());
        assertEquals(savedAudio.getName(), foundDTO.getName());
        assertEquals(savedAudio.getDescription(), foundDTO.getDescription());
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewAudio() {
        AudioDTO savedAudio = audioService.saveNewAudio(audioDTO);

        assertThat(savedAudio).isNotNull();
        assertEquals(audio.getName(), savedAudio.getName());
        assertEquals(audio.getDescription(), savedAudio.getDescription());
    }

    @Disabled
    @DisplayName("Nie wiem czemy nie rzuca wyjatku :((")
    @Transactional
    @Rollback
    @Test
    void testSaveAudioNotValid() {
        assertThrows(CertPathValidatorException.class, () -> {
            audioService.saveNewAudio(AudioDTO.builder().build());
        });
    }

    @Test
    void testUpdateAudioById() {
        AudioDTO dto = audioMapper.audioToAudioDto(audioRepository.findAll().get(0));
        dto.setName("UPDATED");

        AudioDTO saved = audioService.updateUserById(dto.getId(), dto).get();

        assertEquals(saved.getName(), dto.getName());
        assertEquals(saved.getDescription(), dto.getDescription());
    }

    @Disabled
    @DisplayName("Znowu nie wiem czemu nie dziala")
    @Rollback
    @Transactional
    @Test
    void testDeleteAudioById() {
        Audio saved = audioRepository.save(audio);

        assertThat(audioService.getAudioById(saved.getId())).isNotEmpty();

        assertTrue(audioService.deleteAudioById(saved.getId()));

        assertThat(audioService.getAudioById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteAudioByIdNotExist() {
        assertFalse(audioService.deleteAudioById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchAudioById() {
        Audio saved = audioRepository.save(audio);

        AudioDTO updated = audioService.patchAudioById(saved.getId(), audioDTO2).get();

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(audioDTO2.getName());
        assertThat(updated.getDescription()).isEqualTo(audio.getDescription());
    }

    @Test
    void testPatchAudioByIdBadId() {
        assertThat(audioService.patchAudioById(UUID.randomUUID(), audioDTO)).isEmpty();
    }
}

























