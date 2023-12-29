package com.example.waytogo.route.service.impl;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import com.example.waytogo.user.service.api.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class RouteServiceIT {

    @Autowired
    RouteService routeService;

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

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
    void saveNewRouteNotValid() {
        testRouteDTO.setName("");


        assertThrows(ConstraintViolationException.class, () -> {
            RouteDTO routeDTO = routeService.saveNewRoute(testRouteDTO);
        });


    }


    //why I deleted rollback and transactional annotations:
    //https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb
    @Test
    void testRouteExistanceAfterUserDeletion() {

        User user = userRepository.findAll().get(0);
        user.setRoutes(Collections.emptyList());

        Route route = routeRepository.findAll().get(0);

        route.setUser(user);

        userService.deleteUserById(user.getId());

        assertThat(routeRepository.existsById(route.getId())).isTrue();
        assertThat(routeRepository.findById(route.getId()).get().getUser()).isNull();

        //"rollback"
        userRepository.save(user);
        route.setUser(user);
        routeRepository.save(route);

    }


}