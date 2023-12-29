package com.example.waytogo.route.service.impl;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.example.waytogo.user.mapper.UserMapper;
import com.example.waytogo.user.model.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
@Validated
public class RouteServiceJPA implements RouteService {
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    private final UserMapper userMapper;

    private final RouteMapLocationService routeMapLocationService;
    private final RouteMapLocationRepository routeMapLocationRepository;

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
    public Page<RouteDTO> getRoutesByUserId(UUID userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Route> routePage;
        routePage = routeRepository.findByUser_Id(userId, pageRequest);

        return routePage.map(routeMapper::routeToRouteDto);
    }

    @Override
    public RouteDTO saveNewRoute(@Valid RouteDTO routeDTO) {
        return routeMapper.routeToRouteDto(routeRepository.save(routeMapper.routeDtoToRoute(routeDTO)));
    }

    @Override
    public Optional<RouteDTO> updateRouteById(UUID routeId, RouteDTO routeDTO) {
        AtomicReference<Optional<RouteDTO>> atomicReference = new AtomicReference<>();

        routeRepository.findById(routeId).ifPresentOrElse(found -> {
            routeDTO.setId(routeId);
            atomicReference.set(Optional.of(routeMapper
                    .routeToRouteDto(routeRepository
                            .save(routeMapper.routeDtoToRoute(routeDTO)))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteRouteById(UUID routeId) {
        if (routeRepository.existsById(routeId)) {

            routeRepository.deleteById(routeId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<RouteDTO> patchRouteById(UUID routeId, RouteDTO routeDTO) {
        AtomicReference<Optional<RouteDTO>> atomicReference = new AtomicReference<>();

        routeRepository.findById(routeId).ifPresentOrElse(foundRoute -> {
            if (StringUtils.hasText(routeDTO.getName())) {
                foundRoute.setName(routeDTO.getName());
            }
            if (StringUtils.hasText(routeDTO.getDescription())) {
                foundRoute.setDescription(routeDTO.getDescription());
            }
            if(routeDTO.getUser() != null) {
                foundRoute.setUser(userMapper.userDtoToUser(routeDTO.getUser()));
            }
            atomicReference.set(Optional.of(routeMapper.routeToRouteDto(routeRepository.save(foundRoute))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }


    @Override
    public void setUserToNullByUserId(UUID userId) {
        routeRepository.setUserToNullByUserId(userId);
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
