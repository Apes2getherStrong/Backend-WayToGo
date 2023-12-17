package com.example.waytogo.maplocation.repository;

import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.maplocation.model.entity.Coordinates;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({InitializationBasic.class}) // potem dodac tutaj MapLocationCSVServiceImpl
class MapLocationRepositoryTest {
    @Autowired
    MapLocationRepository mapLocationRepository;

    @Test
    void testSaveMapLocation() {
        MapLocation mapLocation =  mapLocationRepository.save(getMapLocation());
        mapLocationRepository.flush();

        assertThat(mapLocation).isNotNull();
        assertThat(mapLocation.getId()).isNotNull();
    }

    @Test
    void testSaveCoordinatesOutOfRange() {
        assertThrows(Exception.class, () -> {
            MapLocation mapLocation =  mapLocationRepository.save(MapLocation.builder()
                    .name("Test Name")
                    .coordinates(Coordinates.builder()
                            .latitude(94.0)
                            .longitude(186.0).build())
                    .build());
            mapLocationRepository.flush();
        });
        assertThrows(Exception.class, () -> {
            MapLocation mapLocation =  mapLocationRepository.save(MapLocation.builder()
                    .name("Test Name")
                    .coordinates(Coordinates.builder()
                            .latitude(-94.0)
                            .longitude(-187.0).build())
                    .build());
            mapLocationRepository.flush();
        });
    }

    @Test
    void testMapLocationNameTooLong() {
        assertThrows(Exception.class, () -> {
            MapLocation mapLocation =  mapLocationRepository.save(MapLocation.builder()
                    .name("My MapLocation 01233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789")
                    .coordinates(getCoordinates())
                    .build());
            mapLocationRepository.flush();
        });
    }

    MapLocation getMapLocation() {
        return MapLocation.builder()
                .name("Test Name")
                .coordinates(getCoordinates())
                .build();
    }
    Coordinates getCoordinates() {
        return Coordinates.builder()
                .latitude(12.0)
                .longitude(13.0).build();
    }
}