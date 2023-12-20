package com.example.waytogo.maplocation.mapper;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapLocationMapperTest {
    @Autowired
    MapLocationMapper mapLocationMapper;

    @Autowired
    GeometryFactory geometryFactory;

    MapLocation mapLocation;
    MapLocationDTO mapLocationDTO;

    @BeforeEach
    void setUp() {
        mapLocation = MapLocation.builder()
                .id(UUID.fromString("6cfc986f-3c82-41ef-bcb6-ff7936e9f600"))
                .name("n")
                .description("d")
                .coordinates(geometryFactory.createPoint(new Coordinate(5.2,11.33)))
                .build();

        mapLocationDTO = MapLocationDTO.builder()
                .id(UUID.fromString("6cfc986f-3c82-41ef-bcb6-ff7936e9f600"))
                .name("n")
                .description("d")
                .coordinates(geometryFactory.createPoint(new Coordinate(5.2,11.33)))
                .build();
    }

    @Test
    void testMapLocationToMapLocationDto() {
        MapLocationDTO mapped = mapLocationMapper.mapLocationToMapLocationDto(mapLocation);

        assertEquals(mapped.getId(), mapLocation.getId());
        assertEquals(mapped.getName(), mapLocation.getName());
        assertEquals(mapped.getDescription(), mapLocation.getDescription());
        assertEquals(mapped.getCoordinates(), mapLocation.getCoordinates());
        assertEquals(mapped.getUpdateDate(), mapLocation.getUpdateDate());
        assertEquals(mapped.getCreatedDate(), mapLocation.getCreatedDate());

    }
}