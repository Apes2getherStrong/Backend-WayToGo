package com.example.waytogo.route.controller;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    void getRoutes() throws Exception {
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
        Page<RouteDTO> page = routeController.getRoutes(1,20);
        assertThat(page.getContent().size()).isEqualTo(0);
    }

    @Test
    void getRouteNotExisting() {
        assertThrows(ResponseStatusException.class, () -> {
            routeController.getRoute(UUID.randomUUID());
        });
    }

    @Test
    void getRoute() throws Exception{

        RouteDTO testRouteDTO = routeMapper.routeToRouteDto(routeRepository.findAll().get(0));


        mockMvc.perform(get(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRouteDTO.getId().toString())))
                .andExpect(jsonPath("$.name", is(testRouteDTO.getName())));
    }

    @Test
    void getRoutesByUserId() throws Exception{

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
    void postRoute() throws Exception {
        RouteDTO testRouteDTO = RouteDTO.builder()
                .name("postRouteITTestName")
                .id(UUID.randomUUID())
                .build();

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Rollback
    @Transactional
    @Test
    void putNewRoute() throws Exception {
        RouteDTO routeDTO = RouteDTO.builder()
                .id(UUID.randomUUID())
                .name("testputname")
                .user(null)
                .build();
        MvcResult result = mockMvc.perform(put(RouteController.ROUTE_PATH_ID, routeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(routeDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        RouteDTO responseRouteDTO = objectMapper.readValue(responseContent, RouteDTO.class);
        assertThat(responseRouteDTO.getName()).isEqualTo(routeDTO.getName());

    }

    @Rollback
    @Transactional
    @Test
    void putRouteUpdate() {

        RouteDTO routeDTO = routeMapper.routeToRouteDto(routeRepository.findAll().get(0));

        ResponseEntity<RouteDTO> responseEntity = routeController.putRoute(routeDTO.getId(), routeDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));

        RouteDTO responseRouteDTO = responseEntity.getBody();
        assertThat(responseRouteDTO).isNotNull();

        Route putRoute = routeRepository.findById(routeDTO.getId()).get();
        assertThat(putRoute.getName()).isEqualTo(routeMapper.routeDtoToRoute(routeDTO).getName());

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

        ResponseEntity responseEntity = routeController.patchRouteById(route.getId(), routeDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Route patchedRoute = routeRepository.findById(route.getId()).get();
        assertThat(patchedRoute.getName()).isEqualTo(newRouteName);
    }
}