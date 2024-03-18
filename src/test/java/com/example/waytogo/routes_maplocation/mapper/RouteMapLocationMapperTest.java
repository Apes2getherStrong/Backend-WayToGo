package com.example.waytogo.routes_maplocation.mapper;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteMapLocationMapperTest {
    @Autowired
    RouteMapLocationMapper routeMapLocationMapper;

    @Autowired
    RouteMapper routeMapper;

    @Autowired
    MapLocationMapper mapLocationMapper;

    RouteMapLocation routeMapLocation;
    RouteMapLocationDTO routeMapLocationDTO;
    MapLocation mapLocation;
    Route route;

    @BeforeEach
    void setUp() {
        routeMapLocation = RouteMapLocation.builder()
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .sequenceNr(2)
                .mapLocation(null)
                .route(null)
                .build();

        route = Route.builder()
                .id(UUID.randomUUID())
                .description("d")
                .name("n")
                .build();

        mapLocation = MapLocation.builder()
                .id(UUID.randomUUID())
                .description("dd")
                .name("nn")
                .build();

        routeMapLocation.setMapLocation(mapLocation);
        routeMapLocation.setRoute(route);

        routeMapLocationDTO = RouteMapLocationDTO.builder()
                .id(UUID.fromString("2aa5e9a8-0e93-4181-87cc-9c40fe1825e6"))
                .sequenceNr(2)
                .mapLocation(mapLocationMapper.mapLocationToMapLocationDto(mapLocation))
                .route(routeMapper.routeToRouteDto(route))
                .build();
    }

    @Test
    void testRouteMapLocationToDto() {
        RouteMapLocationDTO mapped = routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(routeMapLocation);

        assertEquals(mapped.getId(), routeMapLocation.getId());
        assertEquals(mapped.getSequenceNr(), routeMapLocation.getSequenceNr());

        assertEquals(mapped.getMapLocation().getId(), routeMapLocation.getMapLocation().getId());
        assertEquals(mapped.getMapLocation().getName(), routeMapLocation.getMapLocation().getName());
        assertEquals(mapped.getMapLocation().getDescription(), routeMapLocation.getMapLocation().getDescription());

        assertEquals(mapped.getRoute().getId(), routeMapLocation.getRoute().getId());
        assertEquals(mapped.getRoute().getName(), routeMapLocation.getRoute().getName());
        assertEquals(mapped.getRoute().getDescription(), routeMapLocation.getRoute().getDescription());

    }

    @Test
    void testRouteMapLocationDtoToRouteMapLocation() {
        RouteMapLocation mapped = routeMapLocationMapper.routeMapLocationDtoToRouteMapLocation(routeMapLocationDTO);

        assertEquals(mapped.getId(), routeMapLocationDTO.getId());
        assertEquals(mapped.getSequenceNr(), routeMapLocationDTO.getSequenceNr());

        assertEquals(mapped.getMapLocation().getId(), routeMapLocationDTO.getMapLocation().getId());
        assertEquals(mapped.getMapLocation().getName(), routeMapLocationDTO.getMapLocation().getName());
        assertEquals(mapped.getMapLocation().getDescription(), routeMapLocationDTO.getMapLocation().getDescription());

        assertEquals(mapped.getRoute().getId(), routeMapLocationDTO.getRoute().getId());
        assertEquals(mapped.getRoute().getName(), routeMapLocationDTO.getRoute().getName());
        assertEquals(mapped.getRoute().getDescription(), routeMapLocationDTO.getRoute().getDescription());

    }
}