package com.example.waytogo.route.service.api;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.user.model.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface RouteService {

    Page<RouteDTO> getAllRoutes(Integer pageNumber, Integer pageSize);

    Optional<RouteDTO> getRouteById(UUID routeId);

    RouteDTO saveNewRoute(RouteDTO routeDTO);

    RouteDTO updateRouteById(UUID routeId, RouteDTO routeDTO);

    void deleteRouteById(UUID routeId);

    void patchRouteById(UUID routeId, RouteDTO routeDTO);
}
