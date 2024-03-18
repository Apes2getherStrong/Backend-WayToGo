package com.example.waytogo.routes_maplocation.service.api;


import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface RouteMapLocationService {
    Optional<RouteMapLocationDTO> getRouteMapLocationById(UUID routeMapLocationId);

    Page<MapLocationDTO> getAllMapLocationsByRouteId(UUID routeId, Integer pageNumber, Integer pageSize);

    RouteMapLocationDTO saveNewRouteMapLocation(@Valid RouteMapLocationDTO routeMapLocationDTO);

    Optional<RouteMapLocationDTO> updateRouteMapLocationById(UUID routeMapLocationId, @Valid RouteMapLocationDTO routeMapLocationDTO);

    Boolean deleteRouteMapLocationById(UUID routeMapLocationId);
}
