package com.example.waytogo.point.controller;

import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.point.model.dto.CoordinatesDTO;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PointControllerIT {
    @Autowired
    PointController pointController;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointMapper pointMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Test
    @Disabled
    void tenPierwszy() {
    }

    @Rollback
    @Transactional
    @Test
    void saveNewPointTest(){
        PointDTO pointDTO = PointDTO.builder()
                .name("test point")
                .coordinates(
                        CoordinatesDTO.builder()
                                .longitude(12.0)
                                .latitude(16.2)
                                .build()
                )
                .build();

        ResponseEntity<Void> response = pointController.postPoint(pointDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = response.getHeaders().getLocation().getPath().split("/");

        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Point point = pointRepository.findById(savedUUID).get();
        assertThat(point.getId()).isNotNull();


    }

    @Test
    void testPointIdNotFound(){
        assertThrows(ResponseStatusException.class, ()->{
            pointController.getPointById(UUID.randomUUID());
        });
    }
    @Test
    void getPointById(){
        Point point = pointRepository.findAll().get(0);
        PointDTO pointDTO = pointController.getPointById(point.getId()).getBody();

        assertThat(pointDTO).isNotNull();
        assertThat(pointDTO.getId()).isEqualTo(point.getId());
    }
    @Test
    @DisplayName("jak bedzie wiecej danych to odpalic")
    @Disabled
    void listPoints(){
        Page<PointDTO> dtos = pointController.getAllPoints(1,2141).getBody();
        assertThat(dtos.getContent().size()).isEqualTo(1000);
    }

    @Rollback
    @Transactional
    @Test
    @Disabled
    @DisplayName("z usuwaniem problem przez polaczenia z reszta")
    void testEmptyList(){
        pointRepository.deleteAll();
        Page<PointDTO> dtos = pointController.getAllPoints(1,25).getBody();
        assertThat(dtos.getContent().size()).isEqualTo(0);

    }


}
