package com.example.waytogo.point.controller;

import com.example.waytogo.initialize.InitializationBasic;
import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.point.model.dto.CoordinatesDTO;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.point.service.api.PointService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PointService pointService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<PointDTO> beerArgumentCaptor;



    @Test
    @DisplayName("Not implemented Yet testGetAllPoints")
    @Disabled
    public void testGetAllPoints() throws Exception {
        given(pointService.getAllPoints(any(), any()))
                .willReturn(new PageImpl<>(Arrays.asList(PointDTO.builder().build(), PointDTO.builder().build()), PageRequest.of(0, 25), 2L));

        mockMvc.perform(get("/api/v1/points"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getPointById() throws Exception {
        PointDTO testPoint = getPointDTO();

        given(pointService.getPointById(any())).willReturn(Optional.of(testPoint));

        mockMvc.perform(get(PointController.POINT_PATH_ID,testPoint.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPoint.getId().toString())))
                .andExpect(jsonPath("$.coordinates.latitude", is(testPoint.getCoordinates().getLatitude())))
                .andExpect(jsonPath("$.coordinates.longitude", is(testPoint.getCoordinates().getLongitude())))
                .andExpect(jsonPath("$.name", is(testPoint.getName())));
    }

    @Test
    void getPointByIdNotFound() throws Exception {
        given(pointService.getPointById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(PointController.POINT_PATH_ID,UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

   @Test
   void testCreateNewPoint() throws Exception {
        PointDTO pointDTO = getPointDTO();
        pointDTO.setId(null);

        given(pointService.saveNewPoint(any(PointDTO.class))).willReturn(getPointDTO_2());

        mockMvc.perform(post(PointController.POINT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
   }

    @Test
    void testCreatePointNullNameAndNullCoordinates() throws Exception {
        PointDTO pointDTO = PointDTO.builder().build();

        given(pointService.saveNewPoint(any(PointDTO.class))).willReturn(getPointDTO_2());

        MvcResult mvcResult = mockMvc.perform(post(PointController.POINT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void putPointById() {
    }

    @Test
    void deletePointById() {
    }

    @Test
    void patchPointById() {
    }
    PointDTO getPointDTO() {
        return PointDTO.builder()
                .id(UUID.randomUUID())
                .name("test Point")
                .coordinates(CoordinatesDTO.builder().latitude(14.0).longitude(17.2).build())
                .build();
    }
    PointDTO getPointDTO_2() {
        return PointDTO.builder()
                .id(UUID.randomUUID())
                .name("test Point")
                .coordinates(CoordinatesDTO.builder().latitude(14.0).longitude(17.2).build())
                .build();
    }
}