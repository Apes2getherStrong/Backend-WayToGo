package com.example.waytogo.point.repository;

import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.point.model.entity.Coordinates;
import com.example.waytogo.point.model.entity.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({InitializationBasic.class}) // potem dodac tutaj PointCSVServiceImpl
class PointRepositoryTest {
    @Autowired
    PointRepository pointRepository;

    @Test
    void testSavePoint() {
        Point point =  pointRepository.save(getPoint());
        pointRepository.flush();

        assertThat(point).isNotNull();
        assertThat(point.getId()).isNotNull();
    }

    @Test
    void testSaveCoordinatesOutOfRange() {
        assertThrows(Exception.class, () -> {
            Point point =  pointRepository.save(Point.builder()
                    .name("Test Name")
                    .coordinates(Coordinates.builder()
                            .latitude(94.0)
                            .longitude(186.0).build())
                    .build());
            pointRepository.flush();
        });
        assertThrows(Exception.class, () -> {
            Point point =  pointRepository.save(Point.builder()
                    .name("Test Name")
                    .coordinates(Coordinates.builder()
                            .latitude(-94.0)
                            .longitude(-187.0).build())
                    .build());
            pointRepository.flush();
        });
    }

    @Test
    void testPointNameTooLong() {
        assertThrows(Exception.class, () -> {
            Point point =  pointRepository.save(Point.builder()
                    .name("My Point 01233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789012334567890123345678901233456789")
                    .coordinates(getCoordinates())
                    .build());
            pointRepository.flush();
        });
    }

    Point getPoint() {
        return Point.builder()
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