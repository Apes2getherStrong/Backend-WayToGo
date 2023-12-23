package com.example.waytogo.routes_maplocation.service.impl;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Validated
@Primary
@RequiredArgsConstructor
public class RouteMapLocationServiceJPA implements RouteMapLocationService {
    private final RouteMapLocationRepository routeMapLocationRepository;
    private final MapLocationMapper mapLocationMapper;
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @Override
    public Optional<RouteMapLocation> getRouteMapLocationById(UUID routeMapLocationId) {

        return routeMapLocationRepository.findById(routeMapLocationId);
    }

    @Override
    public Page<MapLocationDTO> getAllMapLocationsByRouteId(UUID routeId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<MapLocation> mapLocationPage = routeMapLocationRepository.findMapLocationsByRouteId(routeId ,pageRequest);

        return mapLocationPage.map(mapLocationMapper::mapLocationToMapLocationDto);
    }

    @Override
    public RouteMapLocation saveNewRouteMapLocation(@Valid RouteMapLocation routeMapLocation) {

        return routeMapLocationRepository.save(routeMapLocation);
    }

    @Override
    public Optional<RouteMapLocation> updateRouteMapLocationById(UUID routeMapLocationId, RouteMapLocation routeMapLocation) {
        AtomicReference<Optional<RouteMapLocation>> atomicReference = new AtomicReference<>();
        Optional<RouteMapLocation> test;


        routeMapLocationRepository.findById(routeMapLocationId).ifPresentOrElse(found -> {
            routeMapLocation.setId(routeMapLocationId);
            atomicReference.set(Optional.of(routeMapLocationRepository
                    .save(routeMapLocation)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
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

    @Override
    public void deleteByMapLocationId(UUID mapLocationId) {
        routeMapLocationRepository.deleteByMapLocationId(mapLocationId);
    }

    @Override
    public void deleteByRouteId(UUID routeId) {
        routeMapLocationRepository.deleteByRouteId(routeId);
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
