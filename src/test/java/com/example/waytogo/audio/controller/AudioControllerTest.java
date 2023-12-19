package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.mapper.AudioMapperImpl;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.user.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AudioController.class)
class AudioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AudioService audioService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<AudioDTO> audioDTOArgumentCaptor;

    AudioMapper audioMapper;

    Audio audio1;

    Audio audio2;

    AudioDTO audioDTO1;

    AudioDTO audioDTO2;

    @Autowired
    GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        audioMapper = new AudioMapperImpl();
        audio1 = Audio.builder()
                .id(UUID.randomUUID())
                .name("name1")
                .description("desc1")
                .mapLocation(MapLocation.builder().name("mapLocation1").coordinates(geometryFactory.createPoint(new Coordinate(30.1, 30.2))).build())
                .user(User.builder().id(UUID.randomUUID()).username("user1").build())
                .build();
        audio2 = Audio.builder()
                .id(UUID.randomUUID())
                .name("name2")
                .description("desc2")
                .mapLocation(MapLocation.builder().name("mapLocation2").coordinates(geometryFactory.createPoint(new Coordinate(30.1, 30.2))).build())
                .user(User.builder().username("user2").build())
                .build();

        audioDTO1 = audioMapper.audioToAudioDto(audio1);
        audioDTO2 = audioMapper.audioToAudioDto(audio2);
    }

    @Test
    void testGetAllAudios() throws Exception {
        given(audioService.getAllAudios(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(audioDTO1, audioDTO2))));

        mockMvc.perform(get(AudioController.AUDIO_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    void testGetAudioById() throws Exception {
        given(audioService.getAudioById(any())).willReturn(Optional.of(audioDTO1));

        mockMvc.perform(get(AudioController.AUDIO_PATH_ID, audioDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(audioDTO1.getId().toString())))
                .andExpect(jsonPath("$.name", is(audioDTO1.getName())));
    }

    @Test
    void testGetAudioByIdNotFound() throws Exception {
        given(audioService.getAudioById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(AudioController.AUDIO_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAudiosByUserId() throws Exception {
        given(audioService.getAllAudiosByUserId(any(), any(), any())).willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(audioDTO1))));

        mockMvc.perform(get(AudioController.USER_PATH_ID_AUDIOS, audioDTO1.getUser().getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(1)));
    }

    @Test
    void testCreateAudio() throws Exception {
        given(audioService.saveNewAudio(any())).willReturn(audioDTO1);

         mockMvc.perform(post(AudioController.AUDIO_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioDTO1)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateAudioNullName() throws Exception {
        AudioDTO audioDTONull = AudioDTO.builder().build();

        given(audioService.saveNewAudio(any(AudioDTO.class))).willReturn(audioDTO1);

        MvcResult mvcResult = mockMvc.perform(post(AudioController.AUDIO_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioDTONull)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPutAudioById() throws Exception {
        given(audioService.updateUserById(any(), any())).willReturn(Optional.of(audioDTO1));

        mockMvc.perform(put(AudioController.AUDIO_PATH_ID, audioDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioDTO1)))
                .andExpect(status().isNoContent());
        verify(audioService).updateUserById(any(UUID.class), any(AudioDTO.class));
    }

    @Test
    void testPutAudioByIdNotFound() throws Exception {
        given(audioService.updateUserById(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(put(AudioController.AUDIO_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioDTO1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAudioById() throws Exception {
        given(audioService.deleteAudioById(any())).willReturn(true);

        mockMvc.perform(delete(AudioController.AUDIO_PATH_ID, audioDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(audioService).deleteAudioById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(audioDTO1.getId());
    }

    @Test
    void testDeleteUserByIdNotFound() throws Exception {
        given(audioService.deleteAudioById(any())).willReturn(false);

        mockMvc.perform(delete(AudioController.AUDIO_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchAudioById() throws Exception {
        Map<String, Object> audioMap = new HashMap<>();
        audioMap.put("name", "New Name");

        given(audioService.patchAudioById(any(), any())).willReturn(Optional.of(audioDTO1));

        mockMvc.perform(patch(AudioController.AUDIO_PATH_ID, audioDTO1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioMap)))
                .andExpect(status().isNoContent());

        verify(audioService).patchAudioById(uuidArgumentCaptor.capture(), audioDTOArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(audioDTO1.getId());
        assertThat(audioDTOArgumentCaptor.getValue().getName()).isEqualTo(audioMap.get("name"));
        assertThat(audioDTOArgumentCaptor.getValue().getDescription()).isEqualTo(audioDTO1.getDescription());
    }

    @Test
    void testPatchAudioByIdNotFound() throws Exception {
        Map<String, Object> audioMap = new HashMap<>();
        audioMap.put("name", "New Name");

        given(audioService.patchAudioById(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(patch(AudioController.AUDIO_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audioMap)))
                .andExpect(status().isNotFound());
    }
}


































