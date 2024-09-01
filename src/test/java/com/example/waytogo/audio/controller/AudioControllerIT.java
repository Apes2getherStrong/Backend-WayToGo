package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.user.model.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
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
    MapLocationRepository mapLocationRepository;

    @Autowired
    AudioMapper audioMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    GeometryFactory geometryFactory;

    static MockMultipartFile testFile;
    static String testImagePath = "src/test/java/com/example/waytogo/test_resources/cute_kittens.jpg";
    static String testAudioPath = "src/test/java/com/example/waytogo/test_resources/can_you_hear_me.mp3";

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @BeforeAll
    static void initialization()  throws Exception {
        prepareTestFile(testAudioPath);
    }

    static void prepareTestFile(String filePath)  throws Exception {

        File imageFile = new File(filePath);
        // Check if the file exists
        if (!imageFile.exists()) {
            System.out.println("no file found");
            throw new IOException("Audio file not found");
        }

        testFile = new MockMultipartFile(
                "testAudio",
                "testAudio.mp3",
                "audio/mpeg",
                Files.readAllBytes(imageFile.toPath())
        );
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

        assertThat(Objects.requireNonNull(dtos).getContent().size()).isEqualTo(25);
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
    void testGetAllAudiosByMapLocationId() {
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        System.out.println(mapLocation);

        ResponseEntity<Page<AudioDTO>> response = audioController.getAllAudiosByMapLocationId(mapLocation.getId(),1,25);
        Page<AudioDTO> page = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(page).isNotNull();
        assertThat(page.getSize()).isEqualTo(25);
        assertThat(page.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(page.getContent().size()).isEqualTo(3);
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
            audioController.putAudioById(UUID.randomUUID(), getAudioDto());
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

    @Transactional
    @Test
    void testPatchUserByIdNameTooLong() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);

        audioDTO.setId(null);
        audioDTO.setName("namenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamenamename");

        assertThrows(ConstraintViolationException.class, () -> {
            audioController.patchAudioById(audio.getId(), audioDTO);
        });
    }

    @Transactional
    @Test
    void testPatchUserByIdDescriptionTooLong() {
        Audio audio = audioRepository.findAll().get(0);
        AudioDTO audioDTO = audioMapper.audioToAudioDto(audio);

        audioDTO.setId(null);
        audioDTO.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(audio.getDescription().length());

        assertThrows(ConstraintViolationException.class, () -> {
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


    @Test
    @Rollback
    void testSaveNewAudioFile() throws Exception {
        // Prepare test file as MultipartFile
        MockMultipartFile testFile = new MockMultipartFile(
                "file",
                "testAudio.mp3",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "test audio content".getBytes() // Replace with actual audio bytes if necessary
        );

        Audio testAudio = audioRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = audioController.postAudioFile(testAudio.getId(), testFile);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        testAudio = audioRepository.findById(testAudio.getId()).get(); // Refresh
        assertNotNull(testAudio.getAudioData());
        assertTrue(testAudio.getAudioData().length > 0);
    }


    @Test
    @Rollback
    void testSaveNewAudioFileWrongExtension() throws Exception {
        // Change test file to an image
        File imageFile = new File(testImagePath);
        if (!imageFile.exists()) {
            System.out.println("No file found");
            throw new IOException("Image file not found");
        }

        MockMultipartFile testFile = new MockMultipartFile(
                "file",
                "testImage.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                Files.readAllBytes(imageFile.toPath())
        );

        Audio testAudio = audioRepository.findAll().get(0);

        assertThrows(ResponseStatusException.class, () -> {
            audioController.postAudioFile(testAudio.getId(), testFile);
        });
    }

    @Test
    @Rollback
    void testGetAudioFile() throws Exception {
        // Prepare test file as MultipartFile
        MockMultipartFile testFile = new MockMultipartFile(
                "file",
                "testAudio.mp3",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "test audio content".getBytes() // Replace with actual audio bytes if necessary
        );

        Audio testAudio = audioRepository.findAll().get(0);
        audioService.saveNewAudioFile(testFile, testAudio.getId());

        testAudio = audioRepository.findById(testAudio.getId()).get(); // Refresh

        ResponseEntity<byte[]> responseEntity = audioController.getAudioFile(testAudio.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        byte[] audioData = responseEntity.getBody();
        assertNotNull(audioData);
        assertTrue(audioData.length > 0);
    }

}



























