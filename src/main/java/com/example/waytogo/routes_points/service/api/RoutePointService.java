package com.example.waytogo.routes_points.service.api;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.routes_points.entity.RoutePoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoutePointService {

    List<RoutePoint> getRoutesPointsByRouteId(UUID routeId);

    Optional<RoutePoint> getRoutePointById(UUID routePointId);

    RoutePoint saveNewRoutePoint(RoutePoint routePoint);

    RoutePoint updateRoutePointById(UUID routePointId, RoutePoint routePoint);

    void deleteRoutePointById(UUID routePointId);



}
