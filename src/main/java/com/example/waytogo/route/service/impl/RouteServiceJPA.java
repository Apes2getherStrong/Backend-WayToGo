package com.example.waytogo.route.service.impl;

import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteServiceJPA implements RouteService {
    private final RouteRepository routeRepository;

    @Override
    public Optional<Route> getRouteById(UUID id) {
        return routeRepository.findById(id);
    }

    @Override
    public Route save(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void updateRouteById(UUID routeId, Route route) {
        route.setId(routeId);
        routeRepository.save(route);
    }

    @Override
    public void delete(Route route) {
        routeRepository.delete(route);
    }
}
