package com.example.waytogo.point.service.impl;

import com.example.waytogo.point.model.dto.CoordinatesDTO;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Coordinates;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.point.service.api.PointService;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.routes_points.repository.RoutePointRepository;
import com.example.waytogo.routes_points.service.api.RoutePointService;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointServiceJPATest {
    @Autowired
    PointService pointService;

    @Autowired
    PointRepository pointRepository;


    Point testPoint;
    PointDTO testPointDTO;

    @Transactional
    @Rollback
    @Test
    void isSavedAndInRepository() {
        testPointDTO = getPointDTO();
        PointDTO savedPoint = pointService.saveNewPoint(testPointDTO);

        assertNotNull(savedPoint.getId());

        assertEquals(testPointDTO.getName(), savedPoint.getName());
        assertEquals(testPointDTO.getCoordinates().getLatitude(), savedPoint.getCoordinates().getLatitude());
        assertEquals(testPointDTO.getCoordinates().getLongitude(), savedPoint.getCoordinates().getLongitude());

        assertTrue(pointRepository.existsById(savedPoint.getId()));

    }

    @Transactional
    @Rollback
    @Test
    void savingNotValidEntity() {

        assertThrows(ConstraintViolationException.class, () -> {
            pointService.saveNewPoint(PointDTO.builder().build());
        });
        // bad values for coordinates
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.saveNewPoint(PointDTO.builder()
                    .name("Test Name")
                    .coordinates(CoordinatesDTO.builder()
                            .latitude(-994.0)
                            .longitude(1860.0).build())
                    .build());
        });
        // without coordinates
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.saveNewPoint(PointDTO.builder()
                    .name("Test Name")
                    .build());
        });
        // without name
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.saveNewPoint(PointDTO.builder()
                    .name("Test Name")
                    .coordinates(CoordinatesDTO.builder()
                            .latitude(-994.0)
                            .longitude(1860.0).build())
                    .build());
        });

    }



    PointDTO getPointDTO() {
        return PointDTO.builder()
                .name("testPointttttt")
                .coordinates(CoordinatesDTO.builder().latitude(11.0).longitude(27.2).build())
                .build();
    }
    Point getPoint() {
        return Point.builder()
                .name("Test Name 2")
                .coordinates(Coordinates.builder()
                        .latitude(50.0)
                        .longitude(10.0).build())
                .build();
    }
}