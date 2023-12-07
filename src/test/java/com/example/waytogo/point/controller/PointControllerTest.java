package com.example.waytogo.point.controller;

import com.example.waytogo.point.model.dto.CoordinatesDTO;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.service.api.PointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

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
    ArgumentCaptor<PointDTO> pointArgumentCaptor;



    @Test
    public void testGetAllPoints() throws Exception {
        given(pointService.getAllPoints(any(), any()))
                .willReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(getPointDTO(), getPointDTO_2()))));

        mockMvc.perform(get(PointController.POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()",is(2)));
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
    void putPointById() throws Exception {
        PointDTO pointDTO = getPointDTO();

        given(pointService.updatePointById(any(), any())).willReturn(Optional.of(pointDTO));

        mockMvc.perform(put(PointController.POINT_PATH_ID, pointDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointDTO)))
                .andExpect(status().isNoContent());
        verify(pointService).updatePointById(any(UUID.class), any(PointDTO.class));
    }

    @Test
    void testUpdatePointBlankName() throws Exception {
        PointDTO pointDTO = getPointDTO();
        pointDTO.setName("");

        given(pointService.updatePointById(any(), any())).willReturn(Optional.of(pointDTO));

        MvcResult mvcResult = mockMvc.perform(put(PointController.POINT_PATH_ID, pointDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeletePoint() throws Exception {
        PointDTO pointDTO = getPointDTO();

        given(pointService.deletePointById(any())).willReturn(true);

        mockMvc.perform(delete(PointController.POINT_PATH_ID, pointDTO.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pointService).deletePointById(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(pointDTO.getId());
    }

    @Test
    void testPatchPoint() throws Exception {
        PointDTO pointDTO = getPointDTO();

        Map<String,Object> pointMap = new HashMap<>();
        pointMap.put("name", "New Name");

        given(pointService.patchPointById(any(), any())).willReturn(Optional.of(pointDTO));

        mockMvc.perform(patch(PointController.POINT_PATH_ID, pointDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pointMap)))
                .andExpect(status().isNoContent());

        verify(pointService).patchPointById(uuidArgumentCaptor.capture(), pointArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(pointDTO.getId());
        assertThat(pointArgumentCaptor.getValue().getName()).isEqualTo(pointMap.get("name"));

    }
    PointDTO getPointDTO() {
        return PointDTO.builder()
                .id(UUID.randomUUID())
                .name("test Point")
                .coordinates(CoordinatesDTO.builder().latitude(11.0).longitude(27.2).build())
                .build();
    }
    PointDTO getPointDTO_2() {
        return PointDTO.builder()
                .id(UUID.randomUUID())
                .name("test Point2")
                .coordinates(CoordinatesDTO.builder().latitude(19.0).longitude(17.5).build())
                .build();
    }
}