package com.example.waytogo.routes_points.service.impl;

import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.routes_points.repository.RoutePointRepository;
import com.example.waytogo.routes_points.service.api.RoutePointService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class RoutePointServiceIT {

    @Autowired
    RoutePointService routePointService;

    @Autowired
    RoutePointRepository routePointRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    RouteRepository routeRepository;

    Route testRoute;
    RouteDTO testRouteDTO;
    RoutePoint testRoutePoint;
    Point testPoint;

    @BeforeEach
    void setUp() {

    }

    @Rollback
    @Transactional
    @Test
    void saveNewRoutePointValid() {

        Point point = pointRepository.findAll().get(0);
        Route route = routeRepository.findAll().get(0);

        testRoutePoint = RoutePoint.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .point(point)
                .route(route)
                .sequenceNr(1)
                .build();


        RoutePoint routePoint = routePointService.saveNewRoutePoint(testRoutePoint);
        RoutePoint repositoryRoutePoint = routePointRepository.findById(testRoutePoint.getId()).get();
        assertThat(routePoint.getSequenceNr()).isEqualTo(repositoryRoutePoint.getSequenceNr());


    }

    @Rollback
    @Transactional
    @Test
    void saveNewRoutePointNotValid() {

        Point point = pointRepository.findAll().get(0);
        Route route = routeRepository.findAll().get(0);

        testRoutePoint = RoutePoint.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .point(point)
                .route(route)
                .sequenceNr(1)
                .build();

        testRoutePoint.setSequenceNr(-1);
        assertThrows(ConstraintViolationException.class, () -> {
            RoutePoint routePoint = routePointService.saveNewRoutePoint(testRoutePoint);
        });

    }

    @Rollback
    @Transactional
    @Test
    void getRoutePointByIdNotExisting() {
        Optional<RoutePoint> result = routePointService.getRoutePointById(UUID.randomUUID());
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

    }

    @Rollback
    @Transactional
    @Test
    void getRoutePointByIdExisting() {
        RoutePoint routePoint = routePointRepository.findAll().get(0);
        Optional<RoutePoint> result = routePointService.getRoutePointById(routePoint.getId());
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get().getSequenceNr()).isEqualTo(routePoint.getSequenceNr());

    }


}