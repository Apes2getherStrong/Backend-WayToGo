package com.example.waytogo.route.controller;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.route.service.impl.RouteServiceJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RouteController {
    public static final String ROUTE_PATH = "/api/routes";
    public static final String ROUTE_PATH_ID= ROUTE_PATH + "/{routeId}";
    public static final String ROUTE_PATH_ID_USER = "/api/routes/{userId}/routes";
    private final RouteService routeService;


    @GetMapping(ROUTE_PATH)
    public Page<RouteDTO> getRoutes(@RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize) {
        return routeService.getAllRoutes(pageNumber, pageSize);
    }

    @GetMapping(ROUTE_PATH_ID)
    public ResponseEntity<RouteDTO> getRoute(@PathVariable("routeId") UUID routeId) {
        return ResponseEntity.ok(routeService.getRouteById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(ROUTE_PATH_ID_USER)
    public Page<RouteDTO> getRoutesByUserId(@PathVariable("userId") UUID userId,
                                            @RequestParam(required = false) Integer pageNumber,
                                            @RequestParam(required = false) Integer pageSize) {

        return routeService.getRoutesByUserId(userId, pageNumber, pageSize);

    }

    @PostMapping(ROUTE_PATH)
    public ResponseEntity<RouteDTO> postRoute(@Validated @RequestBody RouteDTO routeDTO){

        RouteDTO savedRoute = routeService.saveNewRoute(routeDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ROUTE_PATH + savedRoute.getId().toString());

        return new ResponseEntity<>(savedRoute, headers, HttpStatus.CREATED);
    }

    @PutMapping(ROUTE_PATH_ID)
    public ResponseEntity<RouteDTO> putRoute(@PathVariable("routeId") UUID routeId, @Validated @RequestBody RouteDTO routeDTO){

        RouteDTO updatedRoute = routeService.updateRouteById(routeId, routeDTO);
        return new ResponseEntity<>(updatedRoute, HttpStatus.CREATED);

    }

    @DeleteMapping(ROUTE_PATH_ID)
    public ResponseEntity<Void> deleteRoute (@PathVariable("routeId") UUID routeId) {

        if( !routeService.deleteRouteById(routeId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PatchMapping(ROUTE_PATH_ID)
    public ResponseEntity<Void> patchRouteById(@PathVariable("routeId") UUID routeId, @RequestBody RouteDTO routeDTO) {
        routeService.patchRouteById(routeId, routeDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
