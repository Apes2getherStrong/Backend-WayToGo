package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RouteMapLocationController {
    public static final String ROUTE_MAP_LOCATION_PATH = "/api/v1/routeMapLocations";
    public static final String ROUTE_MAP_LOCATION_PATH_ID = ROUTE_MAP_LOCATION_PATH + "/{routeMapLocationId}";
    public final static String ROUTE_MAP_LOCATIONS_BY_ROUTE = "/api/v1/routes/{routeId}/routeMapLocations";

    private final RouteMapLocationService routeMapLocationService;

    @GetMapping(ROUTE_MAP_LOCATION_PATH_ID)
    public ResponseEntity<Optional<RouteMapLocation>> getRouteMapLocationById(@PathVariable("routeMapLocationId")UUID routeMapLocationId) {
        Optional<RouteMapLocation> routeMapLocation = routeMapLocationService.getRouteMapLocationById(routeMapLocationId);
        return new ResponseEntity<>(routeMapLocation, HttpStatus.OK);
    }

}
