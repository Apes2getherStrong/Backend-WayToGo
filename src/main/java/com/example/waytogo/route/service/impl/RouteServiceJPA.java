package com.example.waytogo.route.service.impl;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.route.controller.RouteController;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
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

    private void deleteImage(Route route) throws IOException{
        Path directoryPath = Paths.get(RouteController.IMAGE_DIRECTORY_PATH);
        String filename = route.getImageFilename();
        if(filename != null) {

            Path oldFilePath = directoryPath.resolve(filename);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }
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

        byte[] bytes = file.getBytes();

        Path directoryPath = Paths.get(RouteController.IMAGE_DIRECTORY_PATH);
        Files.createDirectories(directoryPath);

        Optional <Route> optRoute = routeRepository.findById(routeId);
        if(optRoute.isEmpty())
            return false;
        Route route = optRoute.get();

        //delete old image
        deleteImage(route);

        String originalFilename = file.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        String newFilename = UUID.randomUUID().toString() + "." + fileExtension;
        String filePath = directoryPath.resolve(newFilename).toString();

        Files.write(Paths.get(filePath), bytes);
        route.setImageFilename(newFilename);
        routeRepository.save(route); //necessary?
        return true;
    }

    //TODO weird function output. Instead of looking at the output of the function, exception handling or
    //TODO controler advisor can be implemented. Disadvantage: exceptions are slower

    //empty optional when route not found, empty array when route found but image not found
    @Override
    public Optional<byte[]> getImageByRouteId(UUID routeId) throws IOException {

        Optional<Route> optRoute = routeRepository.findById(routeId);
        if(optRoute.isEmpty()) {
            //no route
            return Optional.empty();
        }

        String imageFilename = optRoute.get().getImageFilename();

        //no image assigned
        if(imageFilename == null) {
            return Optional.of(new byte[0]);
        }

        Path imagePath = Paths.get(RouteController.IMAGE_DIRECTORY_PATH, imageFilename);

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return Optional.of(imageBytes);
        } else {
            //image assigned but file not found
            return Optional.of(new byte[0]);
        }
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
