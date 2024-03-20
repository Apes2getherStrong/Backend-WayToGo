package com.example.waytogo.routes_maplocation.service.impl;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RouteMapLocationServiceJPATest {
    @Mock
    RouteMapLocationMapper routeMapLocationMapper;

    @Mock
    RouteMapLocationRepository routeMapLocationRepository;

    @InjectMocks
    RouteMapLocationServiceJPA routeMapLocationService;

    Route testRoute;
    RouteDTO testRouteDTO;
    RouteMapLocation testRouteMapLocation;
    RouteMapLocationDTO testRouteMapLocationDTO;
    MapLocation testMapLocation;
    MapLocationDTO testMapLocationDTO;


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

        testMapLocation = MapLocation.builder()
                .id(UUID.fromString("cbf9bb1e-9109-41b8-ae9b-3d8caa1bc823"))
                .name("maplocationtest")
                .build();

        testMapLocationDTO = MapLocationDTO.builder()
                .id(UUID.fromString("cbf9bb1e-9109-41b8-ae9b-3d8caa1bc823"))
                .name("maplocationtest")
                .build();

        testRouteMapLocation = RouteMapLocation.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .mapLocation(testMapLocation)
                .route(testRoute)
                .sequenceNr(1)
                .build();

        testRouteMapLocationDTO = RouteMapLocationDTO.builder()
                .id(UUID.fromString("ad766712-5c9c-4102-ac03-1ae034f029d0"))
                .mapLocation(testMapLocationDTO)
                .route(testRouteDTO)
                .sequenceNr(1)
                .build();
    }


    @Test
    void getRouteMapLocationById() {
        given(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(any())).willReturn(testRouteMapLocationDTO);
        given(routeMapLocationRepository.findById(any(UUID.class))).willReturn(Optional.of(testRouteMapLocation));
        RouteMapLocationDTO routeMapLocationDTO = routeMapLocationService.getRouteMapLocationById(testRouteMapLocation.getId()).get();
        assertThat(routeMapLocationDTO).isEqualTo(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(testRouteMapLocation));
    }

    @Test
    void saveNewRouteMapLocation() {
        given(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(any())).willReturn(testRouteMapLocationDTO);
        given(routeMapLocationMapper.routeMapLocationDtoToRouteMapLocation(any())).willReturn(testRouteMapLocation);
        given(routeMapLocationRepository.save(any(RouteMapLocation.class))).willReturn(testRouteMapLocation);
        RouteMapLocationDTO routeMapLocationDTO = routeMapLocationService.saveNewRouteMapLocation(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(testRouteMapLocation));
        assertThat(routeMapLocationDTO).isNotNull();
    }


    @Test
    void updateRouteMapLocationById() {
        given(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(any())).willReturn(testRouteMapLocationDTO);
        given(routeMapLocationMapper.routeMapLocationDtoToRouteMapLocation(any())).willReturn(testRouteMapLocation);
        given(routeMapLocationRepository.save(any(RouteMapLocation.class))).willReturn(testRouteMapLocation);
        given(routeMapLocationRepository.findById(any(UUID.class))).willReturn(Optional.of(testRouteMapLocation));

        RouteMapLocationDTO mapped = routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(testRouteMapLocation);

        RouteMapLocationDTO routeMapLocationDTO = routeMapLocationService.updateRouteMapLocationById(testRouteMapLocation.getId(), mapped).get();

        RouteMapLocation routeMapLocation = routeMapLocationMapper.routeMapLocationDtoToRouteMapLocation(routeMapLocationDTO);

        assertThat(routeMapLocation).isEqualTo(testRouteMapLocation);
    }

    @Test
    void deleteRouteMapLocationByIdNotExisting() {
        given(routeMapLocationRepository.existsById(any(UUID.class))).willReturn(false);
        Boolean result = routeMapLocationService.deleteRouteMapLocationById(UUID.randomUUID());
        assertThat(result).isFalse();
    }

    @Test
    void deleteRouteMapLocationByIdExisting() {
        given(routeMapLocationRepository.existsById(any(UUID.class))).willReturn(true);
        Boolean result = routeMapLocationService.deleteRouteMapLocationById(UUID.randomUUID());
        assertThat(result).isTrue();
    }
}