package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteMapLocationControllerIT {
    @Autowired
    RouteMapLocationController routeMapLocationController;

    @Autowired
    RouteMapLocationService routeMapLocationService;

    @Autowired
    RouteMapLocationRepository routeMapLocationRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    RouteMapLocationMapper routeMapLocationMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Transactional
    @Test
    void testGetRouteMapLocationById() {
        RouteMapLocation routeMapLocation = routeMapLocationRepository.findAll().get(0);

        ResponseEntity<RouteMapLocationDTO> dto = routeMapLocationController.getRouteMapLocationById(routeMapLocation.getId());

        assertThat(dto.getBody()).isNotNull();
        assertThat(dto.getBody().getSequenceNr()).isEqualTo(routeMapLocation.getSequenceNr());
    }

    @Test
    void testGetRouteMapLocationByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> routeMapLocationController.getRouteMapLocationById(UUID.randomUUID()));
    }

    @Test
    void testGetAllMapLocationsByRouteId() {
        Route route = routeRepository.findAll().get(0);

        ResponseEntity<Page<MapLocationDTO>> dto = routeMapLocationController.getAllMapLocationsByRouteId(route.getId(), 0, 25);

        assertThat(dto.getBody()).isNotNull();
        assertThat(dto.getBody().getSize()).isEqualTo(25);
    }
}