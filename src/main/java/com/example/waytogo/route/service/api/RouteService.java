package com.example.waytogo.route.service.api;

import com.example.waytogo.route.model.entity.Route;

import java.util.Optional;
import java.util.UUID;

public interface RouteService {

    Optional<Route> getRouteById(UUID id);
    Route save(Route route);

    void updateRouteById(UUID routeId, Route route);

    void delete(Route route);
}
