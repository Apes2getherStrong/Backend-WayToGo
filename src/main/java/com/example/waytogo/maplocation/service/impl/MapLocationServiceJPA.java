package com.example.waytogo.maplocation.service.impl;

import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.controller.MapLocationController;
import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import com.example.waytogo.route.controller.RouteController;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class MapLocationServiceJPA implements MapLocationService {
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private final MapLocationMapper mapLocationMapper;
    private final MapLocationRepository mapLocationRepository;

    private final AudioService audioService;
    private final RouteMapLocationService routeMapLocationService;


    @Override
    public MapLocationDTO saveNewMapLocation(MapLocationDTO mapLocation) {
        return mapLocationMapper.mapLocationToMapLocationDto(mapLocationRepository.save(mapLocationMapper.mapLocationDtoToMapLocation(mapLocation)));
    }

    @Override
    public Optional<MapLocationDTO> getMapLocationById(UUID mapLocationId) {
        return mapLocationRepository.findById(mapLocationId).map(mapLocationMapper::mapLocationToMapLocationDto);
    }


    /***
     * Delete mapLocation by id if exists and return True, if not exists return False
     * @param mapLocationId
     * @return Boolean
     */
    @Transactional
    @Override
    public Boolean deleteMapLocationById(UUID mapLocationId) throws IOException {
        if (mapLocationRepository.existsById(mapLocationId)) {

            audioService.deleteAudioByMapLocationId(mapLocationId);
            mapLocationRepository.deleteById(mapLocationId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<MapLocationDTO> updateMapLocationById(UUID mapLocationId, MapLocationDTO mapLocationDTO) {
        AtomicReference<Optional<MapLocationDTO>> atomicReference = new AtomicReference<>();

        mapLocationRepository.findById(mapLocationId).ifPresentOrElse(found -> {
            MapLocationDTO foundDTO = mapLocationMapper.mapLocationToMapLocationDto(found);
            foundDTO.setName(mapLocationDTO.getName());
            foundDTO.setCoordinates(mapLocationDTO.getCoordinates());

            atomicReference.set(Optional.of(mapLocationMapper
                    .mapLocationToMapLocationDto(mapLocationRepository
                            .save(mapLocationMapper.mapLocationDtoToMapLocation(foundDTO)))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Optional<MapLocationDTO> patchMapLocationById(UUID mapLocationId, MapLocationDTO mapLocationDTO) {
        AtomicReference<Optional<MapLocationDTO>> atomicReference = new AtomicReference<>();

        mapLocationRepository.findById(mapLocationId).ifPresentOrElse(mapLocation -> {
            if (StringUtils.hasText(mapLocationDTO.getName())) {
                mapLocation.setName(mapLocationDTO.getName());

                if (mapLocationDTO.getCoordinates() != null) {
                    mapLocation.setCoordinates(mapLocationDTO.getCoordinates());
                }

            }
            atomicReference.set(Optional.of(mapLocationMapper.mapLocationToMapLocationDto(mapLocationRepository.save(mapLocation))));

        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Page<MapLocationDTO> getAllMapLocations(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<MapLocation> mapLocationPage = mapLocationRepository.findAll(pageRequest);

        return mapLocationPage.map(mapLocationMapper::mapLocationToMapLocationDto);
    }


    @Override
    public Boolean saveNewImage(MultipartFile file, UUID mapLocationId) throws IOException {

        byte[] bytes = file.getBytes();

        Path directoryPath = Paths.get(MapLocationController.IMAGE_DIRECTORY_PATH);
        Files.createDirectories(directoryPath);

        Optional <MapLocation> optMapLocation = mapLocationRepository.findById(mapLocationId);
        if(optMapLocation.isEmpty())
            return false;
        MapLocation mapLocation = optMapLocation.get();

        //delete old image
        deleteImage(mapLocation);

        String originalFilename = file.getOriginalFilename();
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        String newFilename = UUID.randomUUID().toString() + "." + fileExtension;
        String filePath = directoryPath.resolve(newFilename).toString();

        Files.write(Paths.get(filePath), bytes);
        mapLocation.setImageFilename(newFilename);
        mapLocationRepository.save(mapLocation); //necessary?
        return true;
    }


    @Override
    public Optional<byte[]> getImageByMapLocationId(UUID mapLocationId) throws IOException {

        Optional<MapLocation> optMapLocation = mapLocationRepository.findById(mapLocationId);
        if(optMapLocation.isEmpty()) {
            //no route
            return Optional.empty();
        }

        String imageFilename = optMapLocation.get().getImageFilename();

        //no image assigned
        if(imageFilename == null) {
            return Optional.of(new byte[0]);
        }

        Path imagePath = Paths.get(MapLocationController.IMAGE_DIRECTORY_PATH, imageFilename);

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return Optional.of(imageBytes);
        } else {
            //image assigned but file not found
            return Optional.of(new byte[0]);
        }
    }

    private void deleteImage(MapLocation mapLocation) throws IOException{
        Path directoryPath = Paths.get(MapLocationController.IMAGE_DIRECTORY_PATH);
        String filename = mapLocation.getImageFilename();
        if(filename != null) {

            Path oldFilePath = directoryPath.resolve(filename);
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }
        }

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

        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
