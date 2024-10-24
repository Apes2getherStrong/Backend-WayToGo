package com.example.waytogo.route.service.impl;

import com.example.waytogo.route.controller.RouteController;
import com.example.waytogo.route.mapper.RouteMapper;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import com.example.waytogo.user.mapper.UserMapper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
@Validated
public class RouteServiceJPA implements RouteService {
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final UserMapper userMapper;
    private final RouteMapLocationService routeMapLocationService;
    private final RouteMapLocationRepository routeMapLocationRepository;

    @Override
    public Page<RouteDTO> getAllRoutes(Integer pageNumber, Integer pageSize, String name) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Route> routePage;
        if(name != null) {
            routePage = routeRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else {
            routePage = routeRepository.findAll(pageRequest);
        }


        return routePage.map(routeMapper::routeToRouteDto);
    }

    @Override
    public Optional<RouteDTO> getRouteById(UUID routeId) {
        return Optional.ofNullable(routeMapper.routeToRouteDto(routeRepository.findById(routeId)
                .orElse(null)));
    }

    @Override
    public Page<RouteDTO> getRoutesByUserId(UUID userId, Integer pageNumber, Integer pageSize, String routeName) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Route> routePage;
        if(routeName != null) {
            routePage = routeRepository.findByNameContainingAndUser_Id(routeName, userId, pageRequest);
        } else {
            routePage = routeRepository.findByUser_Id(userId, pageRequest);
        }

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

    @Transactional
    @Override
    public Boolean deleteRouteById(UUID routeId) throws IOException{
        Optional <Route> optRoute = routeRepository.findById(routeId);
        if(optRoute.isEmpty())
            return false;
        Route route = optRoute.get();

        List<RouteMapLocation> routeMapLocations = routeMapLocationRepository.findByRoute_Id(routeId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();
        for(RouteMapLocation rmp : routeMapLocations) {
            routeMapLocationService.deleteRouteMapLocationById(rmp.getId());
        }

        //delete old image
        deleteImage(route);

        routeRepository.deleteById(routeId);
        return true;

    }

    private void deleteImage(Route route) {
        if (route.getImageData() != null) {
            route.setImageData(null);
            routeRepository.save(route);
        }
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



    //TODO check if routeRepository.save(route); is necessary
    //TODO write tests

    @Override
    public Boolean saveNewImage(MultipartFile file, UUID routeId) throws IOException {
        Optional<Route> optRoute = routeRepository.findById(routeId);
        if (optRoute.isEmpty()) {
            return false;
        }
        Route route = optRoute.get();

        byte[] imageBytes = file.getBytes();
        route.setImageData(imageBytes);

        routeRepository.save(route);
        return true;
    }

    //TODO weird function output. Instead of looking at the output of the function, exception handling or
    //TODO controler advisor can be implemented. Disadvantage: exceptions are slower

    //empty optional when route not found, empty array when route found but image not found
    @Override
    public Optional<byte[]> getImageByRouteId(UUID routeId) throws IOException {
        Optional<Route> optRoute = routeRepository.findById(routeId);
        if (optRoute.isEmpty()) {
            return Optional.empty();
        }

        byte[] imageBytes = optRoute.get().getImageData();
        if (imageBytes == null || imageBytes.length == 0) {
            return Optional.of(new byte[0]);
        }

        return Optional.of(imageBytes);
    }

    @Transactional
    @Override
    public void setUserToNullByUserId(UUID userId) {

        for (Route r : routeRepository.findByUser_Id(userId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
            r.setUser(null);
        }

        //routeRepository.setUserToNullByUserId(userId);
        //for some reason changes made by query are not visible in tests. (check repository for more info)

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
