package com.example.waytogo.route.service.impl;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

//https://www.javaguides.net/2022/03/spring-boot-unit-testing-service-layer.html
@ExtendWith(MockitoExtension.class)
class RouteServiceJPATest {

    @Mock
    RouteRepository routeRepository;

    @Mock
    RouteMapper routeMapper;

    @Mock
    RouteMapLocationRepository routeMapLocationRepository;

    @InjectMocks
    RouteServiceJPA routeService;

    Route testRoute;
    RouteDTO testRouteDTO;

    @BeforeEach
    void setUp() {
        testRoute = Route.builder()
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .name("r1")
                .description("d1")
                .user(null)
                .build();

        testRouteDTO = RouteDTO.builder()
                .user(null)
                .name("r1")
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .build();
    }

    @Test
    void getAllRoutes() {
        given(routeRepository.findAll(any(PageRequest.class))).willReturn(new PageImpl<>(List.of(testRoute)));
        Page<RouteDTO> result = routeService.getAllRoutes(1, 1, null);
        assertThat(result).isNotNull();
        assertThat(result.getSize()).isEqualTo(1);
    }

    @Test
    void getRouteById() {
        given(routeRepository.findById(any(UUID.class))).willReturn(Optional.of(testRoute));
        given(routeMapper.routeToRouteDto(any(Route.class))).willReturn(testRouteDTO);

        Optional<RouteDTO> routeDTO = routeService.getRouteById(testRoute.getId());

        assertThat(routeDTO).isNotNull();
        assertThat(routeDTO).isPresent();
        assertThat(routeDTO.get()).isEqualTo(testRouteDTO);
    }


    @Test
    void saveNewRoute() {
        given(routeMapper.routeDtoToRoute(any(RouteDTO.class))).willReturn(testRoute);
        given(routeRepository.save(any(Route.class))).willReturn(testRoute);
        given(routeMapper.routeToRouteDto(any(Route.class))).willReturn(testRouteDTO);

        RouteDTO routeDTO = routeService.saveNewRoute(testRouteDTO);
        assertThat(routeDTO).isNotNull();
        assertThat(routeDTO).isEqualTo(testRouteDTO);

    }


    @Test
    void updateRouteById() {
        given(routeMapper.routeDtoToRoute(any(RouteDTO.class))).willReturn(testRoute);
        given(routeRepository.save(any(Route.class))).willReturn(testRoute);
        given(routeRepository.findById(any(UUID.class))).willReturn(Optional.of(testRoute));
        given(routeMapper.routeToRouteDto(any(Route.class))).willReturn(testRouteDTO);

        RouteDTO routeDTO = routeService.updateRouteById(testRoute.getId(), testRouteDTO).get();
        assertThat(routeDTO).isNotNull();
        assertThat(routeDTO).isEqualTo(testRouteDTO);

    }

    @Test
    void deleteRouteById() throws IOException {
        given(routeRepository.findById(any(UUID.class))).willReturn(Optional.of(testRoute));
        given( routeMapLocationRepository.findByRoute_Id(any(UUID.class),any(PageRequest.class))).willReturn(Page.empty());

        Boolean result = routeService.deleteRouteById(testRoute.getId());
        assertThat(result).isTrue();
    }

    @Test
    void deleteRouteByIdNotFound() throws IOException{
        given(routeRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        Boolean result = routeService.deleteRouteById(testRoute.getId());
        assertThat(result).isFalse();
    }

    //patch doesn't return anything
    @Disabled
    @DisplayName("OSKAR NAPRAW -Trzeba to dorobic")
    @Test
    void patchRouteById() {
    }

    //same as getAllRoutes()
    @Disabled
    @DisplayName("OSKAR NAPRAW -Trzeba to dorobic")
    @Test
    void getRoutesByUserId() {
    }
}