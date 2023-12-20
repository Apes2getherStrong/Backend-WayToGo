package com.example.waytogo.route.controller;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class RouteControllerIT {
    @Autowired
    RouteController routeController;

    @Autowired
    RouteService routeService;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    void getAllRoutes() throws Exception {
        mockMvc.perform(get(RouteController.ROUTE_PATH)
                        .param("pageNumber", "0")
                        .param("pageSize", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Rollback
    @Transactional
    @Test
    void getRoutesEmpty() {
        routeRepository.deleteAll();
        Page<RouteDTO> page = routeController.getRoutes(1,20).getBody();
        assertThat(page.getContent().size()).isEqualTo(0);
    }

    @Test
    void getRouteNotExisting() {
        assertThrows(ResponseStatusException.class, () -> {
            routeController.getRoute(UUID.randomUUID());
        });
    }

    @Transactional
    @Test
    void getRouteById() throws Exception{

        RouteDTO testRouteDTO = routeMapper.routeToRouteDto(routeRepository.findAll().get(0));


        mockMvc.perform(get(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRouteDTO.getId().toString())))
                .andExpect(jsonPath("$.name", is(testRouteDTO.getName())));
    }


    @Test
    void getRoutesByUserIdEmpty() throws Exception{

        mockMvc.perform(get(RouteController.ROUTE_PATH_ID_USER, UUID.randomUUID())
                        .param("pageNumber", "0")
                        .param("pageSize", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));

    }

    @Rollback
    @Transactional
    @Test
    void getRoutesByUserId() throws Exception{

        User user = userRepository.findAll().get(0);
        user.setRoutes(Collections.emptyList());
        Route route = routeRepository.findAll().get(0);
        route.setUser(user);

        Page<RouteDTO> routePage = routeController.getRoutesByUserId(user.getId(), 0, 10).getBody();
        List<RouteDTO> routes = routePage.stream().toList();

        assertThat(routes.size()).isEqualTo(1);
        assertThat(routes.get(0).getName()).isEqualTo(route.getName());

    }

    @Rollback
    @Transactional
    @Test
    void postRoute() throws Exception {
        RouteDTO testRouteDTO = RouteDTO.builder()
                .name("postRouteITTestName")
                .description("description")
                .id(UUID.randomUUID())
                .build();

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.description", is(testRouteDTO.getDescription())));
    }

    @Rollback
    @Transactional
    @Test
    void postRouteCheckRepository() throws Exception {
        RouteDTO testRouteDTO = RouteDTO.builder()
                .name("postRouteITTestName")
                .description("desc")
                .id(UUID.randomUUID())
                .build();

        ResponseEntity<RouteDTO> responseEntity = routeController.postRoute(testRouteDTO);
        UUID id = responseEntity.getBody().getId();
        Route repositoryRoute = routeRepository.findById(id).get();
        assertThat(testRouteDTO.getName()).isEqualTo(repositoryRoute.getName());
        assertThat(testRouteDTO.getDescription()).isEqualTo(repositoryRoute.getDescription());
    }


    @Rollback
    @Transactional
    @Test
    void putRouteByIdNotFound() throws Exception {
        assertThrows(ResponseStatusException.class, () -> {
            routeController.putRoute(UUID.randomUUID(), RouteDTO.builder()
                    .id(UUID.randomUUID())
                    .name("testputname")
                    .description("desc")
                    .user(null)
                    .build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void putRouteUpdate() {

        RouteDTO routeDTO = routeMapper.routeToRouteDto(routeRepository.findAll().get(0));

        routeDTO.setDescription("UPDATED");

        ResponseEntity<RouteDTO> responseEntity = routeController.putRoute(routeDTO.getId(), routeDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        RouteDTO responseRouteDTO = responseEntity.getBody();
        assertThat(responseRouteDTO).isNotNull();

        Route putRoute = routeRepository.findById(routeDTO.getId()).get();
        assertThat(putRoute.getName()).isEqualTo(routeMapper.routeDtoToRoute(routeDTO).getName());
        assertThat(putRoute.getDescription()).isEqualTo(routeDTO.getDescription());

    }


    @Rollback
    @Transactional
    @Test
    void testDeleteRoute() {
        Route route = routeRepository.findAll().get(0);

        ResponseEntity responseEntity = routeController.deleteRoute(route.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(routeRepository.findById(route.getId())).isEmpty();

    }

    @Test
    void testDeleteRouteNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            routeController.deleteRoute(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchRouteById() {
        Route route = routeRepository.findAll().get(0);
        RouteDTO routeDTO = routeMapper.routeToRouteDto(route);
        routeDTO.setId(null);
        String newRouteName = "NEW NAME";
        routeDTO.setName(newRouteName);
        routeDTO.setUser(null);
        routeDTO.setDescription(null);

        ResponseEntity responseEntity = routeController.patchRouteById(route.getId(), routeDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Route patchedRoute = routeRepository.findById(route.getId()).get();
        assertThat(patchedRoute.getName()).isEqualTo(newRouteName);
        assertThat(patchedRoute.getDescription()).isEqualTo(route.getDescription());
    }

    @Test
    void testPatchRouteByIdNotFound() {
        assertThrows(ResponseStatusException.class, () -> {
            routeController.patchRouteById(UUID.randomUUID(), RouteDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Commit
    @Test
    //@Disabled
    //@DisplayName("kumalale kumalale trzeba dorobić bo naruszenie wiezow integralnosci")
    void testRouteExistanceAfterUserDeletion() {
        //https://stackoverflow.com/questions/5360795/what-is-the-difference-between-unidirectional-and-bidirectional-jpa-and-hibernat
        //https://stackoverflow.com/questions/8434853/jpa-onetomany-update
        User user = userRepository.findAll().get(0);
        user.setRoutes(Collections.emptyList());

        Route newRoute = Route.builder()
                        .user(user)
                        .name("name")
                        .id(UUID.randomUUID())
                        .description(" ")
                        .build();
        routeRepository.save(newRoute);

        user = userRepository.findAll().get(0);


        userRepository.deleteById(user.getId());
        assertThat(routeRepository.existsById(newRoute.getId())).isTrue();

    }

}