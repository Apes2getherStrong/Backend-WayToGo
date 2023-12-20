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
                .id(UUID.fromString("64236c2a-9124-11ee-b9d1-0242ac120002"))
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
    void testRouteToRouteDto() {
        RouteDTO mapped = routeMapper.routeToRouteDto(testRoute);

        assertEquals(mapped.getId(), testRoute.getId());
        assertEquals(mapped.getName(), testRoute.getName());
        assertEquals(mapped.getDescription(), testRoute.getDescription());

        assertEquals(mapped.getUser().getId(), testRoute.getUser().getId());
        assertEquals(mapped.getUser().getPassword(), testRoute.getUser().getPassword());
        assertEquals(mapped.getUser().getLogin(), testRoute.getUser().getLogin());
        assertEquals(mapped.getUser().getUsername(), testRoute.getUser().getUsername());
    }

    @Test
    void testRouteDtoToRoute() {
        Route mapped = routeMapper.routeDtoToRoute(testRouteDTO);

        assertEquals(mapped.getId(), testRouteDTO.getId());
        assertEquals(mapped.getName(), testRouteDTO.getName());
        assertEquals(mapped.getDescription(), testRouteDTO.getDescription());

        assertEquals(mapped.getUser().getId(), testRouteDTO.getUser().getId());
        assertEquals(mapped.getUser().getPassword(), testRouteDTO.getUser().getPassword());
        assertEquals(mapped.getUser().getLogin(), testRouteDTO.getUser().getLogin());
        assertEquals(mapped.getUser().getUsername(), testRouteDTO.getUser().getUsername());
    }
}