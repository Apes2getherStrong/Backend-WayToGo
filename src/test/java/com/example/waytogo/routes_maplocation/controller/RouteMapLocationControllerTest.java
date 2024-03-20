package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@WebMvcTest(RouteMapLocationController.class)
class RouteMapLocationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RouteMapLocationMapper routeMapLocationMapper;

    @MockBean
    RouteMapLocationService routeMapLocationService;

    RouteMapLocation routeMapLocation;

    RouteMapLocationDTO routeMapLocationDTO;

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
    void testGetROuteMapLocationByIdNotFound() throws Exception {
        given(routeMapLocationService.getRouteMapLocationById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(RouteMapLocationController.ROUTE_MAP_LOCATION_PATH_ID, routeMapLocationDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}