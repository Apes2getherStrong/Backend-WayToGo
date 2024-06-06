package com.example.waytogo.route.controller;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.security.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc(addFilters = false)
class RouteControllerTest {
    @MockBean
    JWTService jwtService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RouteService routeService;

    @MockBean
    RouteMapper routeMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<RouteDTO> routeDTOArgumentCaptor;

    RouteDTO testRouteDTO;


    @BeforeEach
    void setUp() {
        testRouteDTO = RouteDTO.builder()
                .name("route1")
                .description("desc")
                .id(UUID.randomUUID())
                .build();

    }


    @Test
    void getRoutes() throws Exception {
        given(routeService.getAllRoutes(any(), any(), any())).willReturn(new PageImpl<>(List.of(testRouteDTO)));

        mockMvc.perform(get(RouteController.ROUTE_PATH)
                        .param("pageNumber", "0")
                        .param("pageSize", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }


    @Test
    void testGetRoute() throws Exception {

        given(routeService.getRouteById(testRouteDTO.getId())).willReturn(Optional.of(testRouteDTO));

        mockMvc.perform(get(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testRouteDTO.getId().toString())))
                .andExpect(jsonPath("$.name", is(testRouteDTO.getName())));
    }

    @Test
    void testGetRouteNotFound() throws Exception {
        given(routeService.getRouteById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(RouteController.ROUTE_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void testGetRoutesByUserId() throws Exception {
        given(routeService.getRoutesByUserId(any(UUID.class), any(), any())).willReturn(new PageImpl<>(List.of(testRouteDTO)));

        mockMvc.perform(get(RouteController.ROUTE_PATH_ID_USER, UUID.randomUUID())
                        .param("pageNumber", "0")
                        .param("pageSize", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));


    }

    @Test
    void testPostRoute() throws Exception {
        given(routeService.saveNewRoute(any(RouteDTO.class))).willReturn(testRouteDTO);

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testPostRouteBlankName() throws Exception {
        testRouteDTO.setName("");
        given(routeService.saveNewRoute(any(RouteDTO.class))).willReturn(testRouteDTO);

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testPostRouteTooLongName() throws Exception {
        testRouteDTO.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        given(routeService.saveNewRoute(any(RouteDTO.class))).willReturn(testRouteDTO);

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPostRouteTooLongDescription() throws Exception {
        testRouteDTO.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        given(routeService.saveNewRoute(any(RouteDTO.class))).willReturn(testRouteDTO);

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isBadRequest());
    }

    //update test
    @Test
    void testPutRoute() throws Exception {
        given(routeService.updateRouteById(any(UUID.class), any(RouteDTO.class))).willReturn(Optional.of(testRouteDTO));

        mockMvc.perform(put(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isCreated());

        verify(routeService).updateRouteById(any(UUID.class), any(RouteDTO.class));
    }

    @Test
    void testPutRouteBlankName() throws Exception {
        testRouteDTO.setName("");
        given(routeService.updateRouteById(any(UUID.class), any(RouteDTO.class))).willReturn(Optional.of(testRouteDTO));

        mockMvc.perform(put(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPutRouteNotFound() throws Exception {
        given(routeService.updateRouteById(any(UUID.class), any(RouteDTO.class))).willReturn(Optional.empty());

        mockMvc.perform(put(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRouteDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRoute() throws Exception {
        given(routeService.deleteRouteById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(routeService).deleteRouteById(uuidArgumentCaptor.capture());
        assertThat(testRouteDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteRouteNotFound() throws Exception {
        given(routeService.deleteRouteById(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(routeService).deleteRouteById(uuidArgumentCaptor.capture());
        assertThat(testRouteDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchRouteById() throws Exception {
        Map<String, Object> routeMap = new HashMap<>();
        routeMap.put("namee", "AAAAAA");

        given(routeService.patchRouteById(any(), any())).willReturn(Optional.of(testRouteDTO));

        mockMvc.perform(patch(RouteController.ROUTE_PATH_ID, testRouteDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeMap)))
                .andExpect(status().isNoContent());

        verify(routeService).patchRouteById(uuidArgumentCaptor.capture(), routeDTOArgumentCaptor.capture());

        assertThat(testRouteDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(routeMap.get("name")).isEqualTo(routeDTOArgumentCaptor.getValue().getName());
    }

    @Test
    void testPatchRouteByIdNotFound() throws Exception {
        Map<String, Object> routeMap = new HashMap<>();
        routeMap.put("namee", "AAAAAA");

        given(routeService.patchRouteById(any(), any())).willReturn(Optional.empty());

        mockMvc.perform(patch(RouteController.ROUTE_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeMap)))
                .andExpect(status().isNotFound());
    }
}