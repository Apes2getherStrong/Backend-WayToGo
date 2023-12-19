package com.example.waytogo.audio.repository;

import com.example.waytogo.audio.controller.AudioController;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.user.model.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(InitializationBasic.class)
class AudioRepositoryTest {

    @Autowired
    AudioRepository audioRepository;

    @Test
    void getAllAudios() {
        List<Audio> list = audioRepository.findAll();

        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void testGetAudioById() {
        Audio audio = Audio.builder()
                .name("n")
                .description("d")
                .build();

        Audio savedAudio = audioRepository.save(audio);
        audioRepository.flush();

        Audio foundAudio = audioRepository.findById(savedAudio.getId()).get();

        assertThat(foundAudio).isNotNull();
        assertThat(foundAudio.getId()).isEqualTo(savedAudio.getId());
        assertThat(foundAudio.getName()).isEqualTo(savedAudio.getName());
        assertThat(foundAudio.getDescription()).isEqualTo(savedAudio.getDescription());
    }

    @Test
    void testSaveAudio() {
        Audio savedAudio = audioRepository.save(Audio.builder()
                .name("n")
                .description("d")
                .build());

        audioRepository.flush();

        assertThat(savedAudio).isNotNull();
        assertThat(savedAudio.getId()).isNotNull();
        assertThat(savedAudio.getId().toString()).isGreaterThan("0");
        assertThat(savedAudio.getName()).isEqualTo("n");
        assertThat(savedAudio.getDescription()).isEqualTo("d");
    }

    @Test
    void testSaveAudioNullName() {
        assertThrows(ConstraintViolationException.class, () -> {
            audioRepository.save(Audio.builder().build());

            audioRepository.flush();
        });
    }

    @Test
    void testSaveAudioNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            audioRepository.save(Audio.builder()
                    .name("namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename")
                    .build());

            audioRepository.flush();
        });
    }

    @Test
    void testSaveAudioDescriptionTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            audioRepository.save(Audio.builder()
                    .description("name")
                    .description("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .build());

            audioRepository.flush();
        });
    }

    @Test
    void testUpdateAudio() {
        Audio savedAudio = audioRepository.save(Audio.builder()
                .name("name")
                .description("desc")
                .build());
        audioRepository.flush();

        savedAudio.setName("Zmienione");

        Audio updatedAudio = audioRepository.save(savedAudio);

        assertThat(savedAudio.getId()).isEqualTo(updatedAudio.getId());
        assertEquals(savedAudio.getName(), updatedAudio.getName());
        assertEquals(savedAudio.getDescription(), updatedAudio.getDescription());
    }

    @Test
    void testDeleteAudio() {
        Audio savedAudio = audioRepository.save(Audio.builder()
                .name("name")
                .build());

        audioRepository.deleteById(savedAudio.getId());

        assertThat(audioRepository.findById(savedAudio.getId())).isEmpty();
    }
}