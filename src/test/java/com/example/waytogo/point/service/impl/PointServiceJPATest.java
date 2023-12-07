package com.example.waytogo.point.service.impl;

import com.example.waytogo.point.mapper.PointMapper;
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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointServiceJPATest {
    @Autowired
    PointService pointService;
    @Autowired
    PointMapper pointMapper;

    @Autowired
    PointRepository pointRepository;


    Point testPoint;
    PointDTO testPointDTO;

    @Transactional
    @Rollback
    @Test
    void testUpdatePointByIdNotExists() {

        testPointDTO = getPointDTO();
        testPointDTO.setId(UUID.randomUUID());
        testPointDTO.setName("Test Name 2");
        testPointDTO.setCoordinates(CoordinatesDTO.builder()
                .latitude(50.0)
                .longitude(10.0).build());

        pointService.updatePointById(testPointDTO.getId(),testPointDTO);

        Optional<Point> point =  pointRepository.findById(testPointDTO.getId());
        assertFalse(point.isPresent());

    }

    @Transactional
    @Rollback
    @Test
    void testUpdatePointById() {
        testPoint = pointRepository.findAll().get(0);

        testPointDTO = pointMapper.pointToPointDto(testPoint);
        testPointDTO.setName("Test Name 2");
        testPointDTO.setCoordinates(CoordinatesDTO.builder()
                .latitude(50.0)
                .longitude(10.0).build());

        pointService.updatePointById(testPoint.getId(),testPointDTO).get();

        Point point = pointRepository.findById(testPoint.getId()).get();

        assertEquals(testPointDTO.getId(), point.getId());
        assertEquals(testPointDTO.getName(), point.getName());
        assertEquals(testPointDTO.getCoordinates().getLatitude(), point.getCoordinates().getLatitude());
        assertEquals(testPointDTO.getCoordinates().getLongitude(), point.getCoordinates().getLongitude());

    }
    @Transactional
    @Rollback
    @Test
    void testUpdatePointByIdBadEntity() {
        testPoint = pointRepository.findAll().get(0);
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.updatePointById(testPoint.getId(),PointDTO.builder().id(testPoint.getId()).build());
        });
        // bad values for coordinates
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.updatePointById(testPoint.getId(),PointDTO.builder()
                    .id(testPoint.getId())
                    .name("Test Name")
                    .coordinates(CoordinatesDTO.builder()
                            .latitude(-994.0)
                            .longitude(1860.0).build())
                    .build());
        });
        // without coordinates
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.updatePointById(testPoint.getId(),PointDTO.builder()
                    .id(testPoint.getId())
                    .name("Test Name")
                    .build());
        });
        // without name
        assertThrows(ConstraintViolationException.class, () -> {
            pointService.updatePointById(testPoint.getId(),PointDTO.builder()
                    .id(testPoint.getId())
                    .name("Test Name")
                    .coordinates(CoordinatesDTO.builder()
                            .latitude(-994.0)
                            .longitude(1860.0).build())
                    .build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testDeleteByIdDeleted() {
        testPoint = pointRepository.findAll().get(0);

        pointService.deletePointById(testPoint.getId());

        assertFalse(pointRepository.existsById(testPoint.getId()));
    }
    @Transactional
    @Rollback
    @Test
    void testDeleteByIdRandomReturnsFalse() {
        assertFalse(pointService.deletePointById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void testGetById() {
        testPoint = getPoint();
        Point savedPoint = pointRepository.save(testPoint);

        PointDTO pointDTO = pointService.getPointById(savedPoint.getId()).get();

        assertEquals(savedPoint.getId(), pointDTO.getId());
        assertEquals(savedPoint.getName(), pointDTO.getName());
        assertEquals(savedPoint.getCoordinates().getLatitude(), pointDTO.getCoordinates().getLatitude());
        assertEquals(savedPoint.getCoordinates().getLongitude(), pointDTO.getCoordinates().getLongitude());
    }

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

    @Test
    @Transactional
    @Rollback
    void patchTest() {
        testPoint = pointRepository.findAll().get(0);

        PointDTO pointDTO = pointMapper.pointToPointDto(testPoint);
        pointDTO.setCoordinates(CoordinatesDTO.builder()
                .latitude(50.0)
                .longitude(10.0).build());

        pointService.patchPointById(testPoint.getId(), pointDTO);

        Point point = pointRepository.findById(testPoint.getId()).get();

        assertEquals(pointDTO.getId(), point.getId());
        assertEquals(pointDTO.getName(), point.getName());
        assertEquals(pointDTO.getCoordinates().getLatitude(), point.getCoordinates().getLatitude());
        assertEquals(pointDTO.getCoordinates().getLongitude(), point.getCoordinates().getLongitude());
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