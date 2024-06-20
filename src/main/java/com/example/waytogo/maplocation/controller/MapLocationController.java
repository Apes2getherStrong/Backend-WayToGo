package com.example.waytogo.maplocation.controller;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import com.example.waytogo.routes_maplocation.service.api.RouteMapLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MapLocationController {
    public final static String MAP_LOCATION_PATH = "/api/v1/mapLocations";
    public final static String MAP_LOCATION_PATH_ID = MAP_LOCATION_PATH + "/{mapLocationId}";
    public final static String MAP_LOCATIONS_BY_ROUTE = "/api/v1/routes/{routeId}/mapLocations";

    //image
    public static final String MAP_LOCATION_PATH_ID_IMAGE = MAP_LOCATION_PATH_ID + "/image";
    public final static String IMAGE_DIRECTORY_PATH = "src/main/java/com/example/waytogo/maplocation/maplocation_images/";


    private final MapLocationService mapLocationService;
    private final RouteMapLocationService routeMapLocationService;

    @GetMapping(MAP_LOCATIONS_BY_ROUTE)
    public ResponseEntity<Page<MapLocationDTO>> getAllMapLocationsByRoute(@PathVariable("routeId") UUID routeId, @RequestParam(required = false) Integer pageNumber,
                                                                          @RequestParam(required = false) Integer pageSize) {

        Page<MapLocationDTO> mapLocationsPage = routeMapLocationService.getAllMapLocationsByRouteId(routeId, pageNumber, pageSize);
        return new ResponseEntity<>(mapLocationsPage, HttpStatus.OK);
    }

    @GetMapping(MAP_LOCATION_PATH)
    public ResponseEntity<Page<MapLocationDTO>> getAllMapLocations(@RequestParam(required = false) Integer pageNumber,
                                                                   @RequestParam(required = false) Integer pageSize) {
        Page<MapLocationDTO> mapLocationsPage = mapLocationService.getAllMapLocations(pageNumber, pageSize);
        return new ResponseEntity<>(mapLocationsPage, HttpStatus.OK);
    }

    @GetMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<MapLocationDTO> getMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId) {
        return ResponseEntity.ok(mapLocationService.getMapLocationById(mapLocationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(MAP_LOCATION_PATH)
    public ResponseEntity<MapLocationDTO> postMapLocation(@Validated @RequestBody MapLocationDTO mapLocationDTO) {
        MapLocationDTO mapLocation = mapLocationService.saveNewMapLocation(mapLocationDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", MAP_LOCATION_PATH + "/" + mapLocation.getId().toString());

        return new ResponseEntity<>(mapLocation, headers, HttpStatus.CREATED);
    }

    @PutMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<MapLocationDTO> putMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId, @Validated @RequestBody MapLocationDTO mapLocationDTO) {
        Optional<MapLocationDTO> updatedMapLocation = mapLocationService.updateMapLocationById(mapLocationId, mapLocationDTO);

        if (updatedMapLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        MapLocationDTO existingMapLocation = updatedMapLocation.get();

        return new ResponseEntity<>(existingMapLocation, HttpStatus.CREATED);
    }

    @DeleteMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<Void> deleteMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId) {
        try {
            if (!mapLocationService.deleteMapLocationById(mapLocationId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.noContent().build();
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping(MAP_LOCATION_PATH_ID)
    public ResponseEntity<Void> patchMapLocationById(@PathVariable("mapLocationId") UUID mapLocationId, @RequestBody MapLocationDTO mapLocationDTO) {
        if (mapLocationService.patchMapLocationById(mapLocationId, mapLocationDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.noContent().build();
    }


    @PutMapping(MAP_LOCATION_PATH_ID_IMAGE)
    @ResponseBody
    public ResponseEntity<Void> putImage(@PathVariable("mapLocationId") UUID mapLocationId,
                                         @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            if (isValidFileExtension(originalFilename)) {
                try {
                    if(mapLocationService.saveNewImage(file, mapLocationId)) {
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    }
                    else {
                        //mapLocation not found
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the file", e);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(MAP_LOCATION_PATH_ID_IMAGE)
    public ResponseEntity<byte[]> getImage(@PathVariable("mapLocationId") UUID mapLocationId) {
        try {

            Optional<byte[]> imageBytesOpt = mapLocationService.getImageByMapLocationId(mapLocationId);
            HttpHeaders headers = new HttpHeaders();

            if (imageBytesOpt.isEmpty()) {
                headers.add("Warning", "Map location not found");
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }

            byte[] imageBytes = imageBytesOpt.get();
            if (imageBytes.length == 0) {
                headers.add("Warning", "Map location image not found");
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }


            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            //headers.setContentDispositionFormData("attachment", filename);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean isValidFileExtension(String fileName) {
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        return Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase());
    }

}
