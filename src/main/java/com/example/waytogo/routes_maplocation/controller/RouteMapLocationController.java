package com.example.waytogo.routes_maplocation.controller;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RouteMapLocationController {
    public static final String ROUTE_MAP_LOCATION_PATH = "/api/v1/routeMapLocations";
    public static final String ROUTE_MAP_LOCATION_PATH_ID = ROUTE_MAP_LOCATION_PATH + "/{routeMapLocationId}";
    public final static String ROUTE_PATH_ID_ROUTE_MAP_LOCATIONS = "/api/v1/routes/{routeId}/routeMapLocations";

    private final RouteMapLocationService routeMapLocationService;

    @GetMapping(ROUTE_MAP_LOCATION_PATH_ID)
    public ResponseEntity<RouteMapLocationDTO> getRouteMapLocationById(@PathVariable("routeMapLocationId")UUID routeMapLocationId) {
        Optional<RouteMapLocationDTO> routeMapLocationDTO = routeMapLocationService.getRouteMapLocationById(routeMapLocationId);

        if (routeMapLocationDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        RouteMapLocationDTO found = routeMapLocationDTO.get();

        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping(ROUTE_PATH_ID_ROUTE_MAP_LOCATIONS)
    public ResponseEntity<Page<MapLocationDTO>> getAllMapLocationsByRouteId(@PathVariable("routeId") UUID routeId,
                                                                                 @RequestParam(required = false) Integer pageNumber,
                                                                                 @RequestParam(required = false) Integer pageSize) {
        Page<MapLocationDTO> foundList = routeMapLocationService.getAllMapLocationsByRouteId(routeId, pageNumber, pageSize);

        return new ResponseEntity<>(foundList, HttpStatus.OK);
    }

    @PostMapping(ROUTE_MAP_LOCATION_PATH)
    public ResponseEntity<RouteMapLocationDTO> postRouteMapLocation(@Validated @RequestBody RouteMapLocationDTO routeMapLocationDTO) {
        RouteMapLocationDTO saved = routeMapLocationService.saveNewRouteMapLocation(routeMapLocationDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ROUTE_MAP_LOCATION_PATH + "/" + saved.getId().toString());

        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }

    @PutMapping(ROUTE_MAP_LOCATION_PATH_ID)
    public ResponseEntity<RouteMapLocationDTO> putRouteMapLocationById(@PathVariable("routeMapLocationId") UUID routeMapLocationId,
                                                                       @RequestBody RouteMapLocationDTO routeMapLocationDTO) {
        if (routeMapLocationService.updateRouteMapLocationById(routeMapLocationId, routeMapLocationDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
