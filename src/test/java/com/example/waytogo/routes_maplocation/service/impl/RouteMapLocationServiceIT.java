package com.example.waytogo.routes_maplocation.service.impl;

import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RouteMapLocationServiceIT {

    @Autowired
    RouteMapLocationService routeMapLocationService;

    @Autowired
    RouteMapLocationRepository routeMapLocationRepository;

    @Autowired
    MapLocationRepository mapLocationRepository;

    @Autowired
    MapLocationService mapLocationService;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    RouteService routeService;

    @Autowired
    GeometryFactory geometryFactory;

    Route testRoute;
    RouteDTO testRouteDTO;
    RouteMapLocation testRouteMapLocation;
    MapLocation testMapLocation;


    @BeforeEach
    void setUp() {

    }

    @Rollback
    @Transactional
    @Test
    void saveNewRouteMapLocationValid() {

        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        Route route = routeRepository.findAll().get(0);

        testRouteMapLocation = RouteMapLocation.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .mapLocation(mapLocation)
                .route(route)
                .sequenceNr(1)
                .build();


        RouteMapLocation routeMapLocation = routeMapLocationService.saveNewRouteMapLocation(testRouteMapLocation);
        RouteMapLocation repositoryRouteMapLocation = routeMapLocationRepository.findById(testRouteMapLocation.getId()).get();
        assertThat(routeMapLocation.getSequenceNr()).isEqualTo(repositoryRouteMapLocation.getSequenceNr());


    }

    @Rollback
    @Transactional
    @Test
    void saveNewRouteMapLocationNotValid() {

        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        Route route = routeRepository.findAll().get(0);

        testRouteMapLocation = RouteMapLocation.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .mapLocation(mapLocation)
                .route(route)
                .sequenceNr(1)
                .build();

        testRouteMapLocation.setSequenceNr(-1);
        assertThrows(ConstraintViolationException.class, () -> {
            RouteMapLocation routeMapLocation = routeMapLocationService.saveNewRouteMapLocation(testRouteMapLocation);
        });

    }

    @Rollback
    @Transactional
    @Test
    void getRouteMapLocationByIdNotExisting() {
        Optional<RouteMapLocation> result = routeMapLocationService.getRouteMapLocationById(UUID.randomUUID());
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

    }

    @Rollback
    @Transactional
    @Test
    void getRouteMapLocationByIdExisting() {
        RouteMapLocation routeMapLocation = routeMapLocationRepository.findAll().get(0);
        Optional<RouteMapLocation> result = routeMapLocationService.getRouteMapLocationById(routeMapLocation.getId());
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get().getSequenceNr()).isEqualTo(routeMapLocation.getSequenceNr());

    }

    @Rollback
    @Transactional
    @Test
    void testRouteMapLocationExistenceAfterRouteDeletion() throws IOException {
        RouteMapLocation rmp = routeMapLocationRepository.findAll().get(0);
        Route route = routeRepository.findAll().get(0);
        rmp.setRoute(route);

        routeService.deleteRouteById(route.getId());
        assertThat(routeMapLocationRepository.existsById(rmp.getId())).isFalse();

    }

    @Rollback
    @Transactional
    @Test
    void testRouteMapLocationExistenceAfterMapLocationDeletion() throws Exception {
        RouteMapLocation rmp = routeMapLocationRepository.findAll().get(0);
        MapLocation mapLocation = mapLocationRepository.findAll().get(0);
        rmp.setMapLocation(mapLocation);

        mapLocationService.deleteMapLocationById(mapLocation.getId());
        assertThat(routeMapLocationRepository.existsById(rmp.getId())).isFalse();

    }


}