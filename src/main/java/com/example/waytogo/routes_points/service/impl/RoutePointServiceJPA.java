package com.example.waytogo.routes_points.service.impl;

import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.routes_points.repository.RoutePointRepository;
import com.example.waytogo.routes_points.service.api.RoutePointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoutePointServiceJPA implements RoutePointService {
    private final RoutePointRepository routePointRepository;
    @Override
    public List<RoutePoint> getRoutesPointsByRouteId(UUID routeId) {
        return routePointRepository.getRoutePointByRouteId(routeId);

    }

    @Override
    public Optional<RoutePoint> getRoutePointById(UUID routePointId) {

        return routePointRepository.findById(routePointId);
    }

    @Override
    public RoutePoint saveNewRoutePoint(RoutePoint routePoint) {

        return routePointRepository.save(routePoint);
    }

    @Override
    public RoutePoint updateRoutePointById(UUID routePointId, RoutePoint routePoint) {

        routePoint.setId(routePointId);
        return this.saveNewRoutePoint(routePoint);
    }

    @Override
    public void deleteRoutePointById(UUID routePointId) {
        routePointRepository.deleteById(routePointId);

    }
}
