package com.example.waytogo.route.service.api;

import com.example.waytogo.route.model.dto.RouteDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface RouteService {

    Page<RouteDTO> getAllRoutes(Integer pageNumber, Integer pageSize);

    Optional<RouteDTO> getRouteById(UUID routeId);

    Page<RouteDTO> getRoutesByUserId(UUID userId, Integer pageNumber, Integer pageSize);

    Page<RouteDTO> getRoutesByName(String name, Integer pageNumber, Integer pageSize);

    RouteDTO saveNewRoute(@Valid RouteDTO routeDTO);

    Optional<RouteDTO> updateRouteById(UUID routeId, @Valid RouteDTO routeDTO);

    Boolean deleteRouteById (UUID routeId) throws IOException;

    Optional<RouteDTO> patchRouteById(UUID routeId, RouteDTO routeDTO);

    void setUserToNullByUserId(UUID userId);

    Boolean saveNewImage (MultipartFile file, UUID routeId) throws IOException;

    Optional<byte[]> getImageByRouteId(UUID routeId) throws IOException;
}
