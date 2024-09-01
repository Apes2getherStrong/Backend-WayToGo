package com.example.waytogo.route.controller;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.service.api.RouteService;
import com.example.waytogo.route.service.impl.RouteServiceJPA;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RestController
public class RouteController {
    public static final String ROUTE_PATH = "/api/v1/routes";
    public static final String ROUTE_PATH_ID = ROUTE_PATH + "/{routeId}";
    public static final String ROUTE_PATH_ID_USER = "/api/v1/routes/{userId}/routes";
    public static final String ROUTE_PATH_ID_IMAGE = ROUTE_PATH_ID + "/image";
    //TODO change  the path (it shouldn't be hardcoded)
    public static final String IMAGE_DIRECTORY_PATH = "src/main/java/com/example/waytogo/route/route_images/";

    private final RouteService routeService;


    @GetMapping(ROUTE_PATH)
    public ResponseEntity<Page<RouteDTO>> getRoutes(@RequestParam(required = false) Integer pageNumber,
                                                    @RequestParam(required = false) Integer pageSize,
                                                    @RequestParam(required = false) String routeName) {
        return new ResponseEntity<>(routeService.getAllRoutes(pageNumber, pageSize, routeName), HttpStatus.OK);
    }

    @GetMapping(ROUTE_PATH_ID)
    public ResponseEntity<RouteDTO> getRoute(@PathVariable("routeId") UUID routeId) {
        return ResponseEntity.ok(routeService.getRouteById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(ROUTE_PATH_ID_USER)
    public ResponseEntity<Page<RouteDTO>> getRoutesByUserId(@PathVariable("userId") UUID userId,
                                                            @RequestParam(required = false) Integer pageNumber,
                                                            @RequestParam(required = false) Integer pageSize,
                                                            @RequestParam(required = false) String routeName) {

        return new ResponseEntity<>(routeService.getRoutesByUserId(userId, pageNumber, pageSize, routeName), HttpStatus.OK);

    }

    @PostMapping(ROUTE_PATH)
    public ResponseEntity<RouteDTO> postRoute(@Validated @RequestBody RouteDTO routeDTO) {

        RouteDTO savedRoute = routeService.saveNewRoute(routeDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ROUTE_PATH + "/" + savedRoute.getId().toString());

        return new ResponseEntity<>(savedRoute, headers, HttpStatus.CREATED);
    }

    @PutMapping(ROUTE_PATH_ID_IMAGE)
    @ResponseBody
    public ResponseEntity<Void> putImage(@PathVariable("routeId") UUID routeId,
                                         @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            if (isValidFileExtension(originalFilename)) {
                try {
                    if (routeService.saveNewImage(file, routeId)) {
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    } else {
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


    @GetMapping(ROUTE_PATH_ID_IMAGE)
    public ResponseEntity<byte[]> getImage(@PathVariable("routeId") UUID routeId) {
        try {

            Optional<byte[]> imageBytesOpt = routeService.getImageByRouteId(routeId);
            HttpHeaders headers = new HttpHeaders();

            if (imageBytesOpt.isEmpty()) {
                headers.add("Warning", "Route not found");
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }

            byte[] imageBytes = imageBytesOpt.get();
            if (imageBytes.length == 0) {
                headers.add("Warning", "Route image not found");
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

    @PutMapping(ROUTE_PATH_ID)
    public ResponseEntity<RouteDTO> putRoute(@PathVariable("routeId") UUID routeId, @Validated @RequestBody RouteDTO routeDTO) {

        Optional<RouteDTO> updatedRoute = routeService.updateRouteById(routeId, routeDTO);

        if (updatedRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        RouteDTO existingRoute = updatedRoute.get();

        return new ResponseEntity<>(existingRoute, HttpStatus.CREATED);

    }

    @DeleteMapping(ROUTE_PATH_ID)
    public ResponseEntity<Void> deleteRoute(@PathVariable("routeId") UUID routeId) {

        try {
            if (!routeService.deleteRouteById(routeId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping(ROUTE_PATH_ID)
    public ResponseEntity<Void> patchRouteById(@PathVariable("routeId") UUID routeId, @RequestBody RouteDTO routeDTO) {
        if (routeService.patchRouteById(routeId, routeDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
