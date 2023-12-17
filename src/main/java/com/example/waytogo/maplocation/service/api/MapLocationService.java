package com.example.waytogo.maplocation.service.api;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
public interface MapLocationService {
    MapLocationDTO saveNewMapLocation(@Valid MapLocationDTO mapLocation);

    Optional<MapLocationDTO> getMapLocationById(UUID mapLocationId);

    Page<MapLocationDTO> getAllMapLocations(Integer pageNumber, Integer pageSize);

    Boolean deleteMapLocationById(UUID mapLocationId);

    Optional<MapLocationDTO> updateMapLocationById(UUID mapLocationId, @Valid MapLocationDTO mapLocationDTO);

    Optional<MapLocationDTO> patchMapLocationById(UUID mapLocationId, MapLocationDTO mapLocationDTO);
}
