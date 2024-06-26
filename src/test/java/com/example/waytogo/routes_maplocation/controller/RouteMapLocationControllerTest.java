package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.example.waytogo.security.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(RouteMapLocationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RouteMapLocationControllerTest {

    @MockBean
    JWTService jwtService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RouteMapLocationMapper routeMapLocationMapper;

    @MockBean
    RouteMapLocationService routeMapLocationService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<AudioDTO> audioDTOArgumentCaptor;

    RouteMapLocation routeMapLocation;

    RouteMapLocationDTO routeMapLocationDTO;

    MapLocationDTO mapLocationDTO;

    @BeforeEach
    void setUp() {
        routeMapLocation = RouteMapLocation.builder()
                .id(UUID.fromString("ccc4cadf-e4eb-4203-8ed5-ec9f60eacdb9"))
                .sequenceNr(4)
                .route(null)
                .mapLocation(null)
                .build();

        routeMapLocationDTO = RouteMapLocationDTO.builder()
                .id(UUID.fromString("bb0f73c9-c522-47ac-b104-21ec814670b8"))
                .sequenceNr(4)
                .route(null)
                .mapLocation(null)
                .build();

        mapLocationDTO = MapLocationDTO.builder()
                .id(UUID.fromString("0db98ec0-3d06-43f8-8412-c0caa2594705"))
                .name("n")
                .description("d")
                .build();
    }

    @Test
    void testGetRouteMapLocationById() throws Exception {
        given(routeMapLocationService.getRouteMapLocationById(any())).willReturn(Optional.of(routeMapLocationDTO));

        mockMvc.perform(get(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(routeMapLocationDTO.getId().toString())))
                .andExpect(jsonPath("$.sequenceNr", is(routeMapLocationDTO.getSequenceNr())));
    }

    @Test
    void testGetRouteMapLocationByIdNotFound() throws Exception {
        given(routeMapLocationService.getRouteMapLocationById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMapLocationsByRouteId() throws Exception {
        given(routeMapLocationService.getAllMapLocationsByRouteId(any(), any(), any())).willReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(mapLocationDTO))));

        mockMvc.perform(get(RouteMapLocationController.ROUTE_PATH_ID_ROUTE_MAP_LOCATIONS, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()", is(1)));
    }

    @Test
    void testCreateRouteMapLocation() throws Exception {
        given(routeMapLocationService.saveNewRouteMapLocation(any())).willReturn(routeMapLocationDTO);

        mockMvc.perform(post(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeMapLocationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateRouteMapLocationNullSequenceNrNull() throws Exception {
        RouteMapLocationDTO dto = RouteMapLocationDTO.builder().build();

        given(routeMapLocationService.saveNewRouteMapLocation(any())).willReturn(routeMapLocationDTO);

        MvcResult mvcResult = mockMvc.perform(post(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPutRouteMapLocationById() throws Exception {
        given(routeMapLocationService.updateRouteMapLocationById(any(), any())).willReturn(Optional.of(routeMapLocationDTO));

        mockMvc.perform(put(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeMapLocationDTO)))
                .andExpect(status().isNoContent());
        verify(routeMapLocationService).updateRouteMapLocationById(any(UUID.class), any(RouteMapLocationDTO.class));
    }

    @Test
    void testPutRouteMapLocationByIdNotFound() throws Exception {
        given(routeMapLocationService.updateRouteMapLocationById(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(put(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeMapLocationDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRouteMapLocationById() throws Exception {
        given(routeMapLocationService.deleteRouteMapLocationById(any())).willReturn(true);

        mockMvc.perform(delete(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(routeMapLocationService).deleteRouteMapLocationById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(routeMapLocationDTO.getId());
    }

    @Test
    void testDeleteRouteMapLocationByIdNotFound() throws Exception {
        given(routeMapLocationService.deleteRouteMapLocationById(any())).willReturn(false);

        mockMvc.perform(delete(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
