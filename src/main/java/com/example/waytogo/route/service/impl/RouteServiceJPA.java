package com.example.waytogo.route.service.impl;

import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteServiceJPA implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<RouteDTO> getAllRoutes(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Route> routePage;
        routePage = routeRepository.findAll(pageRequest);

        return routePage.map(routeMapper::routeToRouteDto);
    }

    @Override
    public Optional<RouteDTO> getRouteById(UUID routeId) {
        return Optional.ofNullable(routeMapper.routeToRouteDto(routeRepository.findById(routeId)
                .orElse(null)));
    }

    @Override
    public RouteDTO saveNewRoute(RouteDTO routeDTO) {
        return routeMapper.routeToRouteDto(routeRepository.save(routeMapper.routeDtoToRoute(routeDTO)));
    }

    @Override
    public RouteDTO updateRouteById(UUID routeId, RouteDTO routeDTO) {
        routeDTO.setId(routeId);
        return this.saveNewRoute(routeDTO);
    }

    @Override
    public void deleteRouteById(UUID routeId) {
        routeRepository.deleteById(routeId);
    }

    @Override
    public void patchRouteById(UUID routeId, RouteDTO routeDTO) {
        routeRepository.findById(routeId).ifPresent(foundRoute -> {
            if (StringUtils.hasText(routeDTO.getName())) {
                foundRoute.setName(routeDTO.getName());
            }
            routeRepository.save(foundRoute);
        });
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("name"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
