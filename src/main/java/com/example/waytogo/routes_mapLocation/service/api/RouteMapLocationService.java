package com.example.waytogo.routes_mapLocation.service.api;


import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteMapLocationService {

    List<RouteMapLocation> getRoutesMapLocationsByRouteId(UUID routeId);

    Optional<RouteMapLocation> getRouteMapLocationById(UUID routeMapLocationId);

    RouteMapLocation saveNewRouteMapLocation(@Valid RouteMapLocation routeMapLocation);

    RouteMapLocation updateRouteMapLocationById(UUID routeMapLocationId, RouteMapLocation routeMapLocation);

    Boolean deleteRouteMapLocationById(UUID routeMapLocationId);



}
