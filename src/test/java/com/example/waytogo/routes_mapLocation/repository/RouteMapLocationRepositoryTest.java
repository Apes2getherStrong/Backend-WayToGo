package com.example.waytogo.routes_mapLocation.repository;

import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RouteMapLocationRepositoryTest {

    @Autowired
    RouteMapLocationRepository routeMapLocationRepository;

    RouteMapLocation testRouteMapLocation;

    @BeforeEach
    void setUp() {
        testRouteMapLocation = RouteMapLocation.builder()
                .mapLocation(null)
                .route(null)
                .sequenceNr(1)
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    void getRouteMapLocationByRouteId() {
        RouteMapLocation routeMapLocation = routeMapLocationRepository.save(testRouteMapLocation);
        assertThat(routeMapLocation.getSequenceNr()).isEqualTo(testRouteMapLocation.getSequenceNr());
        assertThat(routeMapLocation.getId()).isEqualTo(testRouteMapLocation.getId());
    }
}