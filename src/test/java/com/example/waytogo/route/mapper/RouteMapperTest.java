package com.example.waytogo.route.mapper;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.model.dto.UserDTO;
import com.example.waytogo.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteMapperTest {
    @Autowired
    RouteMapper routeMapper;

    @Autowired
    UserMapper userMapper;

    Route testRoute;
    RouteDTO testRouteDTO;
    UserDTO userDTO;

    @BeforeEach
    void setUp() {

        testRoute = Route.builder()
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .name("r1")
                .user(null)
                .build();

        User user = User.builder()
                .userId(UUID.fromString("64236c2a-9124-11ee-b9d1-0242ac120002"))
                .routes(List.of(testRoute))
                .login("login")
                .password("password")
                .audios(List.of())
                .username("username")
                .build();

        testRoute.setUser(user);

        userDTO = userMapper.userToUserDto(user);


        testRouteDTO = RouteDTO.builder()
                .user(userDTO)
                .name("r1")
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .build();
    }

    @Test
    void testMapping() {
        RouteDTO routeDTO = routeMapper.routeToRouteDto(testRoute);
        assertThat(routeDTO).isEqualTo(testRouteDTO);
        assertThat(routeDTO.getUser()).isEqualTo(userDTO);
    }
}