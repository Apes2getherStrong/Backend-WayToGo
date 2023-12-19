package com.example.waytogo.routes_maplocation.service.api;


import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface RouteMapLocationService {
    Page<MapLocationDTO> getAllMapLocationsByRouteId(UUID routeId, Integer pageNumber, Integer pageSize);
    Optional<RouteMapLocation> getRouteMapLocationById(UUID routeMapLocationId);

    RouteMapLocation saveNewRouteMapLocation(@Valid RouteMapLocation routeMapLocation);

    RouteMapLocation updateRouteMapLocationById(UUID routeMapLocationId, RouteMapLocation routeMapLocation);

    Boolean deleteRouteMapLocationById(UUID routeMapLocationId);



}
