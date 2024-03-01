package com.example.waytogo.maplocation.controller;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MapLocationController {
    public final static String MAP_LOCATION_PATH = "/api/v1/mapLocations";
    public final static String MAP_LOCATION_PATH_ID = MAP_LOCATION_PATH + "/{mapLocationId}";
    public final static String MAP_LOCATIONS_BY_ROUTE = "/api/v1/routes/{routeId}/mapLocations";

    private final MapLocationService mapLocationService;
    private final RouteMapLocationService routeMapLocationService;

    @GetMapping(MAP_LOCATIONS_BY_ROUTE)
    public ResponseEntity<Page<MapLocationDTO>> getAllMapLocationsByRoute(@PathVariable("routeId") UUID routeId, @RequestParam(required = false) Integer pageNumber,
                                                                          @RequestParam(required = false) Integer pageSize) {

        Page<MapLocationDTO> mapLocationsPage = routeMapLocationService.getAllMapLocationsByRouteId(routeId, pageNumber, pageSize);
        return new ResponseEntity<>(mapLocationsPage, HttpStatus.OK);
    }

    @GetMapping(MAP_LOCATION_PATH)
    public ResponseEntity<Page<MapLocationDTO>> getAllMapLocations(@RequestParam(required = false) Integer pageNumber,
                                                                   @RequestParam(required = false) Integer pageSize) {
        Page<MapLocationDTO> mapLocationsPage = mapLocationService.getAllMapLocations(pageNumber, pageSize);
        return new ResponseEntity<>(mapLocationsPage, HttpStatus.OK);
    }

    @GetMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<MapLocationDTO> getMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId) {
        return ResponseEntity.ok(mapLocationService.getMapLocationById(mapLocationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(MAP_LOCATION_PATH)
    public ResponseEntity<Void> postMapLocation(@Validated @RequestBody MapLocationDTO mapLocationDTO) {
        MapLocationDTO mapLocation = mapLocationService.saveNewMapLocation(mapLocationDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", MAP_LOCATION_PATH + "/" + mapLocation.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<Void> putMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId, @Validated @RequestBody MapLocationDTO mapLocationDTO) {
        if (mapLocationService.updateMapLocationById(mapLocationId, mapLocationDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<Void> deleteMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId) {
        if (!mapLocationService.deleteMapLocationById(mapLocationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<Void> patchMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId, @RequestBody MapLocationDTO mapLocationDTO) {
        if (mapLocationService.patchMapLocationById(mapLocationId, mapLocationDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }

}
