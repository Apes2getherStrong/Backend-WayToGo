package com.example.waytogo.maplocation.service.impl;

import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.maplocation.service.api.MapLocationService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapLocationServiceJPATest {
    @Autowired
    MapLocationService mapLocationService;
    @Autowired
    MapLocationMapper mapLocationMapper;

    @Autowired
    MapLocationRepository mapLocationRepository;

    @Autowired
    GeometryFactory geometryFactory;


    MapLocation testMapLocation;
    MapLocationDTO testMapLocationDTO;

    @Transactional
    @Rollback
    @Test
    void testUpdateMapLocationByIdNotExists() {

        testMapLocationDTO = getMapLocationDTO();
        testMapLocationDTO.setId(UUID.randomUUID());
        testMapLocationDTO.setName("Test Name 2");

        mapLocationService.updateMapLocationById(testMapLocationDTO.getId(), testMapLocationDTO);

        Optional<MapLocation> mapLocation = mapLocationRepository.findById(testMapLocationDTO.getId());
        assertFalse(mapLocation.isPresent());

    }

    @Transactional
    @Rollback
    @Test
    void testUpdateMapLocationById() {
        testMapLocation = mapLocationRepository.findAll().get(0);

        testMapLocationDTO = mapLocationMapper.mapLocationToMapLocationDto(testMapLocation);
        testMapLocationDTO.setName("Test Name 2");

        mapLocationService.updateMapLocationById(testMapLocation.getId(), testMapLocationDTO).get();

        MapLocation mapLocation = mapLocationRepository.findById(testMapLocation.getId()).get();

        assertEquals(testMapLocationDTO.getId(), mapLocation.getId());
        assertEquals(testMapLocationDTO.getName(), mapLocation.getName());
        assertEquals(testMapLocationDTO.getDescription(), testMapLocation.getDescription());


    }

    @Transactional
    @Rollback
    @Test
    void testUpdateMapLocationByIdBadEntity() {
        testMapLocation = mapLocationRepository.findAll().get(0);
        assertThrows(ConstraintViolationException.class, () -> {
            mapLocationService.updateMapLocationById(testMapLocation.getId(), MapLocationDTO.builder().id(testMapLocation.getId()).build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteByIdDeleted() {
        testMapLocation = mapLocationRepository.findAll().get(0);

        mapLocationService.deleteMapLocationById(testMapLocation.getId());

        assertFalse(mapLocationRepository.existsById(testMapLocation.getId()));
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteByIdRandomReturnsFalse() {
        assertFalse(mapLocationService.deleteMapLocationById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void testGetById() {
        testMapLocation = getMapLocation();
        MapLocation savedMapLocation = mapLocationRepository.save(testMapLocation);

        MapLocationDTO mapLocationDTO = mapLocationService.getMapLocationById(savedMapLocation.getId()).get();

        assertEquals(savedMapLocation.getId(), mapLocationDTO.getId());
        assertEquals(savedMapLocation.getName(), mapLocationDTO.getName());
        assertEquals(savedMapLocation.getDescription(), mapLocationDTO.getDescription());

    }

    @Transactional
    @Rollback
    @Test
    void isSavedAndInRepository() {
        testMapLocationDTO = getMapLocationDTO();
        MapLocationDTO savedMapLocation = mapLocationService.saveNewMapLocation(testMapLocationDTO);

        assertNotNull(savedMapLocation.getId());

        assertEquals(testMapLocationDTO.getName(), savedMapLocation.getName());
        assertEquals(testMapLocationDTO.getDescription(), savedMapLocation.getDescription());


        assertTrue(mapLocationRepository.existsById(savedMapLocation.getId()));

    }

    @Transactional
    @Rollback
    @Test
    void savingNotValidEntity() {

        assertThrows(ConstraintViolationException.class, () -> {
            mapLocationService.saveNewMapLocation(MapLocationDTO.builder().build());
        });

    }

    @Test
    @Transactional
    @Rollback
    void patchTest() {
        testMapLocation = mapLocationRepository.findAll().get(0);

        MapLocationDTO mapLocationDTO = mapLocationMapper.mapLocationToMapLocationDto(testMapLocation);


        mapLocationService.patchMapLocationById(testMapLocation.getId(), mapLocationDTO);

        MapLocation mapLocation = mapLocationRepository.findById(testMapLocation.getId()).get();

        assertEquals(mapLocationDTO.getId(), mapLocation.getId());
        assertEquals(mapLocationDTO.getName(), mapLocation.getName());
        assertEquals(mapLocationDTO.getDescription(), mapLocation.getDescription());

    }

    MapLocationDTO getMapLocationDTO() {
        return MapLocationDTO.builder()
                .name("testMapLocationttttt")
                .description("descDTO")
                .coordinates(geometryFactory.createPoint(new Coordinate(25.2, 13.6)))
                .build();
    }

    MapLocation getMapLocation() {
        return MapLocation.builder()
                .name("Test Name 2")
                .description("desc")
                .coordinates(geometryFactory.createPoint(new Coordinate(25.2, 13.6)))
                .build();
    }
}