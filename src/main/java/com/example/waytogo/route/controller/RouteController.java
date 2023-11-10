package com.example.waytogo.route.controller;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.GetRouteResponse;
import com.example.waytogo.route.model.dto.PostRouteRequest;
import com.example.waytogo.route.model.dto.PutRouteRequest;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.service.impl.RouteServiceJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RouteController {
    public final String ROUTE_PATH = "api/routes";
    public final String ROUTE_PATH_ID= ROUTE_PATH + "/{routeID}";
    private final RouteServiceJPA routeService;
    private final RouteMapper routeMapper;

    @GetMapping(ROUTE_PATH)
    public ResponseEntity getRoutes() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(ROUTE_PATH_ID)
    public ResponseEntity<GetRouteResponse> getRoute(@PathVariable("routeId") UUID routeId) {
        Optional<Route> optionalRoute = routeService.getRouteById(routeId);
        if (optionalRoute.isPresent()) {
            return new ResponseEntity<>(routeMapper.routeToGetRouteResponse(optionalRoute.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(ROUTE_PATH)
    public ResponseEntity postRoute(@RequestBody PostRouteRequest route){

        Route newRoute = routeMapper.postRouteRequestToRoute(route);
        newRoute.setId(UUID.randomUUID());

        Route savedRoute = routeService.save(newRoute);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ROUTE_PATH + "/" + savedRoute.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(ROUTE_PATH_ID)
    public ResponseEntity<Void> putRoute(@PathVariable("routeId") UUID routeId, @RequestBody PutRouteRequest route){
        routeService.updateRouteById(routeId, routeMapper.putRouteRequestToRoute(route));
        return new ResponseEntity<>(null, HttpStatus.CREATED);

    }

    @DeleteMapping(ROUTE_PATH_ID)
    public ResponseEntity deleteRoute (@PathVariable("routeId") UUID routeId) {

        Optional<Route> optionalRoute = routeService.getRouteById(routeId);

        if (optionalRoute.isPresent()) {
            routeService.delete(optionalRoute.get());
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
