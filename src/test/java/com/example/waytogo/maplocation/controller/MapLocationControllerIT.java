package com.example.waytogo.maplocation.controller;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.CoordinatesDTO;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MapLocationControllerIT {
    @Autowired
    MapLocationController mapLocationController;

    @Autowired
    MapLocationRepository mapLocationRepository;

    @Autowired
    MapLocationMapper mapLocationMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    @Disabled
    void tenPierwszy() {
    }
    @Test
    @Disabled
    @DisplayName("z usuwaniem problem przez polaczenia z reszta")
    void testyNaListBrakuje(){

    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            mapLocationController.deleteMapLocationById(UUID.randomUUID());
        });
    }

    // no ogolnie to cos mi tu nie dzialalo jak chcialem parametry coordinates zmienic w sensie to do zapisywania jakos nie sprawdzalo poprawnosci zbytnio
    @Test
    void testPatchMapLocationBadName() throws Exception {
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);

        Map<String, Object> mapLocationMap = new HashMap<>();

        mapLocationMap.put("name", "abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890abcdefghijklmnoprstw1234567890");

        MvcResult result = mockMvc.perform(patch(MapLocationController.MAP_LOCATION_PATH_ID, mapLocation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());


    }

    @Rollback
    @Transactional
    @Test
    void testGetByIdNotFound() {
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = mapLocationController.deleteMapLocationById(mapLocation.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(mapLocationRepository.findById(mapLocation.getId())).isEmpty();
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            mapLocationController.putMapLocationById(UUID.randomUUID(), MapLocationDTO.builder().name("test").coordinates(CoordinatesDTO.builder().longitude(12.2).latitude(13.2).build()).build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingMapLocation() throws JsonProcessingException {
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        MapLocationDTO mapLocationDTO = mapLocationMapper.mapLocationToMapLocationDto(mapLocation);

        final String newName = "changed name";
        final Double newLongitude = 0.9213123;
        final Double newLatitude = 63.324213;


        mapLocationDTO.setName("changed name");
        mapLocationDTO.setCoordinates(CoordinatesDTO.builder()
                .longitude(newLongitude)
                .latitude(newLatitude)
                .build());


        ResponseEntity<Void> responseEntity = mapLocationController.putMapLocationById(mapLocation.getId(), mapLocationDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        MapLocation updatedMapLocation = mapLocationRepository.findById(mapLocation.getId()).get();
        assertThat(updatedMapLocation.getCoordinates().getLatitude()).isEqualTo(newLatitude);
        assertThat(updatedMapLocation.getCoordinates().getLongitude()).isEqualTo(newLongitude);


    }

    @Rollback
    @Transactional
    @Test
    void saveNewMapLocationTest() {
        MapLocationDTO mapLocationDTO = MapLocationDTO.builder()
                .name("test mapLocation")
                .coordinates(
                        CoordinatesDTO.builder()
                                .longitude(12.0)
                                .latitude(16.2)
                                .build()
                )
                .build();

        ResponseEntity<Void> response = mapLocationController.postMapLocation(mapLocationDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = response.getHeaders().getLocation().getPath().split("/");

        UUID savedUUID = UUID.fromString(locationUUID[4]);

        MapLocation mapLocation = mapLocationRepository.findById(savedUUID).get();
        assertThat(mapLocation.getId()).isNotNull();


    }

    @Test
    void testMapLocationIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            mapLocationController.getMapLocationById(UUID.randomUUID());
        });
    }

    @Test
    void getMapLocationById() {
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        MapLocationDTO mapLocationDTO = mapLocationController.getMapLocationById(mapLocation.getId()).getBody();

        assertThat(mapLocationDTO).isNotNull();
        assertThat(mapLocationDTO.getId()).isEqualTo(mapLocation.getId());
    }

    @Test
    @DisplayName("jak bedzie wiecej danych to odpalic")
    @Disabled
    void listMapLocations() {
        Page<MapLocationDTO> dtos = mapLocationController.getAllMapLocations(1, 2141).getBody();
        assertThat(dtos.getContent().size()).isEqualTo(1000);
    }

    @Rollback
    @Transactional
    @Test
    @Disabled
    @DisplayName("z usuwaniem problem przez polaczenia z reszta")
    void testEmptyList() {
        mapLocationRepository.deleteAll();
        Page<MapLocationDTO> dtos = mapLocationController.getAllMapLocations(1, 25).getBody();
        assertThat(dtos.getContent().size()).isEqualTo(0);

    }


}
