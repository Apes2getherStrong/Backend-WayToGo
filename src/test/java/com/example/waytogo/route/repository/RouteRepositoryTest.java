package com.example.waytogo.route.repository;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RouteRepositoryTest {

    @Autowired
    RouteRepository routeRepository;

    Route testRoute;
    RouteDTO testRouteDTO;

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
    }

    @Test
    void testSave() {
        Route route = routeRepository.save(testRoute);
        assertThat(route.getName()).isEqualTo(testRoute.getName());
        assertThat(route.getUser()).isEqualTo(testRoute.getUser());
        assertThat(route.getId()).isEqualTo(testRoute.getId());
    }


}