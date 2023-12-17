package com.example.waytogo.routes_mapLocation.service.impl;

import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import com.example.waytogo.routes_mapLocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_mapLocation.service.api.RouteMapLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class RouteMapLocationServiceJPA implements RouteMapLocationService {
    private final RouteMapLocationRepository routeMapLocationRepository;
    @Override
    public List<RouteMapLocation> getRoutesMapLocationsByRouteId(UUID routeId) {
        return routeMapLocationRepository.getRouteMapLocationByRouteId(routeId);

    }

    @Override
    public Optional<RouteMapLocation> getRouteMapLocationById(UUID routeMapLocationId) {

        return routeMapLocationRepository.findById(routeMapLocationId);
    }

    @Override
    public RouteMapLocation saveNewRouteMapLocation(@Valid RouteMapLocation routeMapLocation) {

        return routeMapLocationRepository.save(routeMapLocation);
    }

    @Override
    public RouteMapLocation updateRouteMapLocationById(UUID routeMapLocationId, RouteMapLocation routeMapLocation) {

        routeMapLocation.setId(routeMapLocationId);
        return this.saveNewRouteMapLocation(routeMapLocation);
    }

    @Override
    public Boolean deleteRouteMapLocationById(UUID routeMapLocationId) {
        if(routeMapLocationRepository.existsById(routeMapLocationId))
        {
            routeMapLocationRepository.deleteById(routeMapLocationId);
            return true;
        }

        return false;
    }
}
