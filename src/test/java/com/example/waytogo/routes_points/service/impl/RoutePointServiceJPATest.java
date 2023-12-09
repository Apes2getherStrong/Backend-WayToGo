package com.example.waytogo.routes_points.service.impl;

import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.impl.RouteServiceJPA;
import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.routes_points.repository.RoutePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoutePointServiceJPATest {

    @Mock
    RoutePointRepository routePointRepository;

    @InjectMocks
    RoutePointServiceJPA routePointService;

    Route testRoute;
    RouteDTO testRouteDTO;
    RoutePoint testRoutePoint;
    Point testPoint;

    @BeforeEach
    void setUp() {
        testRoute = Route.builder()
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .name("r1")
                .user(null)
                .build();

        testRouteDTO = RouteDTO.builder()
                .user(null)
                .name("r1")
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .build();

        testPoint = Point.builder()
                .id(UUID.fromString("cbf9bb1e-9109-41b8-ae9b-3d8caa1bc823"))
                .name("pointtest")
                .build();

        testRoutePoint = RoutePoint.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .point(testPoint)
                .sequenceNr(1)
                .build();
    }



    @Test
    void getRoutePointById() {
        given(routePointRepository.findById(any(UUID.class))).willReturn(Optional.of(testRoutePoint));
        RoutePoint routePoint = routePointService.getRoutePointById(testRoutePoint.getId()).get();
        assertThat(routePoint).isEqualTo(testRoutePoint);
    }

    @Test
    void saveNewRoutePoint() {
        given(routePointRepository.save(any(RoutePoint.class))).willReturn(testRoutePoint);
        RoutePoint routePoint = routePointService.saveNewRoutePoint(testRoutePoint);
        assertThat(routePoint).isNotNull();
    }


    @Test
    void updateRoutePointById() {
        given(routePointRepository.save(any(RoutePoint.class))).willReturn(testRoutePoint);
        RoutePoint routePoint = routePointService.updateRoutePointById(testRoutePoint.getId(), testRoutePoint);
        assertThat(routePoint).isEqualTo(testRoutePoint);
    }

    @Test
    void deleteRoutePointByIdNotExisting() {
        given(routePointRepository.existsById(any(UUID.class))).willReturn(false);
        Boolean result = routePointService.deleteRoutePointById(UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void deleteRoutePointByIdExisting() {
        given(routePointRepository.existsById(any(UUID.class))).willReturn(true);
        Boolean result = routePointService.deleteRoutePointById(UUID.randomUUID());
        assertThat(result).isTrue();
    }
}