package com.example.waytogo.maplocation.repository;

import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.initialize.csvLoading.service.CsvServiceLoader;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({InitializationBasic.class, CsvServiceLoader.class , CsvConverterGeneric.class})
class MapLocationRepositoryTest {


    @Autowired
    MapLocationRepository mapLocationRepository;

    @Autowired
    GeometryFactory geometryFactory;

    @Test
    void testSaveMapLocation() {
        MapLocation mapLocation = mapLocationRepository.save(getMapLocation());
        mapLocationRepository.flush();

        assertThat(mapLocation).isNotNull();
        assertThat(mapLocation.getId()).isNotNull();
    }


    @Test
    void testMapLocationNameTooLong() {
        assertThrows(Exception.class, () -> {
            MapLocation mapLocation = mapLocationRepository.save(MapLocation.builder()
                    .name("My MapLocation 01233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789")
                    .coordinates(geometryFactory.createPoint(new Coordinate(300, 300)))
                    .build());
            mapLocationRepository.flush();
        });

        assertThrows(Exception.class, () -> {
            MapLocation mapLocation = mapLocationRepository.save(MapLocation.builder()
                    .name("My MapLocation 456789")
                    .coordinates(geometryFactory.createPoint(new Coordinate(300, 300)))
                    .build());
            mapLocationRepository.flush();
        });
    }

    @Test
    void testMapLocationDescriptionTooLong() {
        assertThrows(Exception.class, () -> {
            MapLocation mapLocation = mapLocationRepository.save(MapLocation.builder()
                    .name("My MapLocation")
                    .description("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .coordinates(geometryFactory.createPoint(new Coordinate(300, 300)))
                    .build());
            mapLocationRepository.flush();
        });
    }

    MapLocation getMapLocation() {
        return MapLocation.builder()
                .name("Test Name")
                .description("desc")
                .coordinates(geometryFactory.createPoint(new Coordinate(22.2, 31.2)))
                .build();
    }
}