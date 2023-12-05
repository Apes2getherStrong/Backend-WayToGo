package com.example.waytogo.routes_points.repository;

import com.example.waytogo.routes_points.entity.RoutePoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoutePointRepositoryTest {

    @Autowired
    RoutePointRepository routePointRepository;

    RoutePoint testRoutePoint;

    @BeforeEach
    void setUp() {
        testRoutePoint = RoutePoint.builder()
                .point(null)
                .route(null)
                .sequenceNr(1)
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    void getRoutePointByRouteId() {
        RoutePoint routePoint = routePointRepository.save(testRoutePoint);
        assertThat(routePoint.getSequenceNr()).isEqualTo(testRoutePoint.getSequenceNr());
        assertThat(routePoint.getId()).isEqualTo(testRoutePoint.getId());
    }
}