package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.example.waytogo.user.model.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
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
    MapLocationRepository mapLocationRepository;

    @Autowired
    RouteMapLocationMapper routeMapLocationMapper;

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    MapLocationMapper mapLocationMapper;

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

    @Rollback
    @Transactional
    @Test
    void testSaveNewRouteMapLocation() {
        MapLocation foundMapLocation = mapLocationRepository.findAll().get(0);
        Route foundRoute = routeRepository.findAll().get(0);

        RouteMapLocationDTO dto = RouteMapLocationDTO.builder()
                .id(UUID.fromString("904a9e98-2a34-4643-bd2c-7b83844a939e"))
                .sequenceNr(5)
                .mapLocation(mapLocationMapper.mapLocationToMapLocationDto(foundMapLocation))
                .route(routeMapper.routeToRouteDto(foundRoute))
                .build();

        ResponseEntity<RouteMapLocationDTO> responseEntity = routeMapLocationController.postRouteMapLocation(dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        System.out.println(responseEntity.getHeaders().getLocation().getPath().split("/")[4]);
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        RouteMapLocation saved =  routeMapLocationRepository.findById(savedUUID).get();
        assertThat(saved).isNotNull();
        assertThat(saved.getSequenceNr()).isEqualTo(dto.getSequenceNr());

    }

    @Transactional
    @Test
    void testUpdateExistingRouteMapLocation() {
        RouteMapLocation found = routeMapLocationRepository.findAll().get(0);
        RouteMapLocationDTO foundDTO = routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(found);
        foundDTO.setId(null);

        final Integer sequenceNumber = 10;
        foundDTO.setSequenceNr(sequenceNumber);

        assertThat(foundDTO.getSequenceNr()).isNotEqualTo(found.getSequenceNr());

        ResponseEntity<RouteMapLocationDTO> responseEntity = routeMapLocationController.putRouteMapLocationById(found.getId(), foundDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        RouteMapLocation updated = routeMapLocationRepository.findById(found.getId()).get();
        assertThat(updated.getSequenceNr()).isEqualTo(sequenceNumber);
        assertThat(updated.getMapLocation()).isEqualTo(found.getMapLocation());
    }

    @Transactional
    @Test
    void testUpdateExistingRouteMapLocationNotFound() {
        RouteMapLocation routeMapLocation = routeMapLocationRepository.findAll().get(0);
        RouteMapLocationDTO dto = routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(routeMapLocation);
        assertThrows(ResponseStatusException.class, () -> {
            routeMapLocationController.putRouteMapLocationById(UUID.randomUUID(), dto);
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteExistingRouteMapLocation() {
        RouteMapLocation found = routeMapLocationRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = routeMapLocationController.deleteRouteMapLocationById(found.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(routeMapLocationRepository.findById(found.getId())).isEmpty();
    }

    @Test
    void testDeleteExistingRouteMapLocationNotDFound() {
        assertThrows(ResponseStatusException.class, () -> {
            routeMapLocationController.deleteRouteMapLocationById(UUID.randomUUID());
        });
    }
}