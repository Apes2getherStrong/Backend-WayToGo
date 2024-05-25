package com.example.waytogo.maplocation.controller;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.example.waytogo.security.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(MapLocationController.class)
@AutoConfigureMockMvc(addFilters = false)
class MapLocationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JWTService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MapLocationService mapLocationService;
    @MockBean
    RouteMapLocationService routeMapLocationService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<MapLocationDTO> mapLocationArgumentCaptor;

    @Autowired
    GeometryFactory geometryFactory;

    @Test
    @Disabled
    @DisplayName("JANEK NAPRAWdorobic test do tego szukania po route punktow")
    void aaa() {
    }

    @Test
    public void testGetAllMapLocations() throws Exception {
        given(mapLocationService.getAllMapLocations(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(getMapLocationDTO(), getMapLocationDTO_2()))));

        mockMvc.perform(get(MapLocationController.MAP_LOCATION_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(2)));
    }

    @Test
    @DisplayName("nie ma tutaj coordow sprawdzania")
    void getMapLocationById() throws Exception {
        MapLocationDTO testMapLocation = getMapLocationDTO();

        given(mapLocationService.getMapLocationById(any())).willReturn(Optional.of(testMapLocation));

        mockMvc.perform(get(MapLocationController.MAP_LOCATION_PATH_ID, testMapLocation.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testMapLocation.getId().toString())))
                .andExpect(jsonPath("$.name", is(testMapLocation.getName())))
                .andExpect(jsonPath("$.description", is(testMapLocation.getDescription())));
    }

    @Test
    void getMapLocationByIdNotFound() throws Exception {
        given(mapLocationService.getMapLocationById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(MapLocationController.MAP_LOCATION_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewMapLocation() throws Exception {
        MapLocationDTO mapLocationDTO = getMapLocationDTO();
        mapLocationDTO.setId(null);

        given(mapLocationService.saveNewMapLocation(any(MapLocationDTO.class))).willReturn(getMapLocationDTO_2());

        mockMvc.perform(post(MapLocationController.MAP_LOCATION_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateMapLocationNullNameAndNullCoordinates() throws Exception {
        MapLocationDTO mapLocationDTO = MapLocationDTO.builder().build();

        given(mapLocationService.saveNewMapLocation(any(MapLocationDTO.class))).willReturn(getMapLocationDTO_2());

        MvcResult mvcResult = mockMvc.perform(post(MapLocationController.MAP_LOCATION_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void putMapLocationById() throws Exception {
        MapLocationDTO mapLocationDTO = getMapLocationDTO();

        given(mapLocationService.updateMapLocationById(any(), any())).willReturn(Optional.of(mapLocationDTO));

        mockMvc.perform(put(MapLocationController.MAP_LOCATION_PATH_ID, mapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationDTO)))
                .andExpect(status().isNoContent());
        verify(mapLocationService).updateMapLocationById(any(UUID.class), any(MapLocationDTO.class));
    }

    @Test
    void testUpdateMapLocationBlankName() throws Exception {
        MapLocationDTO mapLocationDTO = getMapLocationDTO();
        mapLocationDTO.setName("");

        given(mapLocationService.updateMapLocationById(any(), any())).willReturn(Optional.of(mapLocationDTO));

        MvcResult mvcResult = mockMvc.perform(put(MapLocationController.MAP_LOCATION_PATH_ID, mapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteMapLocation() throws Exception {
        MapLocationDTO mapLocationDTO = getMapLocationDTO();

        given(mapLocationService.deleteMapLocationById(any())).willReturn(true);

        mockMvc.perform(delete(MapLocationController.MAP_LOCATION_PATH_ID, mapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(mapLocationService).deleteMapLocationById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(mapLocationDTO.getId());
    }

    @Test
    @DisplayName("dodac zeby sprawdzalo coordy tez")
    void testPatchMapLocation() throws Exception {
        MapLocationDTO mapLocationDTO = getMapLocationDTO();

        Map<String, Object> mapLocationMap = new HashMap<>();
        mapLocationMap.put("name", "New Name");

        given(mapLocationService.patchMapLocationById(any(), any())).willReturn(Optional.of(mapLocationDTO));

        mockMvc.perform(patch(MapLocationController.MAP_LOCATION_PATH_ID, mapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapLocationMap)))
                .andExpect(status().isNoContent());

        verify(mapLocationService).patchMapLocationById(uuidArgumentCaptor.capture(), mapLocationArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(mapLocationDTO.getId());
        assertThat(mapLocationArgumentCaptor.getValue().getName()).isEqualTo(mapLocationMap.get("name"));

    }

    MapLocationDTO getMapLocationDTO() {
        return MapLocationDTO.builder()
                .id(UUID.randomUUID())
                .name("test MapLocation")
                .description("desc")
                .coordinates(geometryFactory.createPoint(new Coordinate(54.2, 85.2)))
                .build();
    }

    MapLocationDTO getMapLocationDTO_2() {
        return MapLocationDTO.builder()
                .id(UUID.randomUUID())
                .name("test MapLocation2")
                .description("desc")
                .coordinates(geometryFactory.createPoint(new Coordinate(54.2, 85.2)))
                .build();
    }
}