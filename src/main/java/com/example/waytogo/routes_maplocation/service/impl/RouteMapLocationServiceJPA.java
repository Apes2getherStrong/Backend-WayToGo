package com.example.waytogo.routes_maplocation.service.impl;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.routes_maplocation.mapper.RouteMapLocationMapper;
import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Validated
@Primary
@RequiredArgsConstructor
public class RouteMapLocationServiceJPA implements RouteMapLocationService {
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final RouteMapLocationMapper routeMapLocationMapper;
    private final RouteMapLocationRepository routeMapLocationRepository;
    private final MapLocationMapper mapLocationMapper;

    @Override
    public Optional<RouteMapLocationDTO> getRouteMapLocationById(UUID routeMapLocationId) {

        return Optional.ofNullable(routeMapLocationMapper.routeMapLocationToRouteMapLocationDto(routeMapLocationRepository.findById(routeMapLocationId)
                .orElse(null)));
    }

    public List<RouteMapLocationDTO> getRouteMapLocationByMapLocationId(UUID mapLocationId) {

        return routeMapLocationRepository.findByMapLocation_Id(mapLocationId)
                .stream()
                .map(routeMapLocationMapper::routeMapLocationToRouteMapLocationDto)
                .collect(Collectors.toList());
    }

    public List<RouteMapLocationDTO> getRouteMapLocationByMapLocationIdAndRouteId(UUID mapLocationId, UUID routeId) {

        return routeMapLocationRepository.findByMapLocation_IdAndRoute_Id(mapLocationId, routeId)
                .stream()
                .map(routeMapLocationMapper::routeMapLocationToRouteMapLocationDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MapLocationDTO> getAllMapLocationsByRouteId(UUID routeId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<MapLocation> mapLocationPage = routeMapLocationRepository.findMapLocationsByRouteId(routeId, pageRequest);

        return mapLocationPage.map(mapLocationMapper::mapLocationToMapLocationDto);
    }

    @Override
    public RouteMapLocationDTO saveNewRouteMapLocation(@Valid RouteMapLocationDTO routeMapLocationDTO) {

        return routeMapLocationMapper
                .routeMapLocationToRouteMapLocationDto(routeMapLocationRepository
                .save(routeMapLocationMapper
                .routeMapLocationDtoToRouteMapLocation(routeMapLocationDTO)));
    }

    @Override
    public Optional<RouteMapLocationDTO> updateRouteMapLocationById(UUID routeMapLocationId, @Valid RouteMapLocationDTO routeMapLocationDTO) {
        AtomicReference<Optional<RouteMapLocationDTO>> atomicReference = new AtomicReference<>();
        Optional<RouteMapLocation> test;


        routeMapLocationRepository.findById(routeMapLocationId).ifPresentOrElse(found -> {
            routeMapLocationDTO.setId(routeMapLocationId);
            atomicReference.set(Optional.of(routeMapLocationMapper
                    .routeMapLocationToRouteMapLocationDto(routeMapLocationRepository
                            .save(routeMapLocationMapper
                                    .routeMapLocationDtoToRouteMapLocation(routeMapLocationDTO)))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteRouteMapLocationById(UUID routeMapLocationId) {
        if (routeMapLocationRepository.existsById(routeMapLocationId)) {
            routeMapLocationRepository.deleteById(routeMapLocationId);
            return true;
        }

        return false;
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = DEFAULT_PAGE;
        int queryPageSize = DEFAULT_PAGE_SIZE;
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        }
        if (pageSize != null && pageSize > 0) {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "sequenceNr");

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
