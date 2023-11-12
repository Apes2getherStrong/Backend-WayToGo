package com.example.waytogo.point.controller;

import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.service.api.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController {
    private final static String POINT_PATH = "/api/v1/points";
    private final static String POINT_PATH_ID = POINT_PATH + "/{pointId}";
    private final PointService pointService;

    @GetMapping(POINT_PATH)
    public ResponseEntity<Page<PointDTO>> getAllPoints(@RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Integer pageSize) {
        Page<PointDTO> pointsPage = pointService.getAllPoints(pageNumber, pageSize);
        return new ResponseEntity<>(pointsPage, HttpStatus.OK);
    }
    @GetMapping(POINT_PATH_ID)
    public ResponseEntity<PointDTO> getPointById(@PathVariable("pointId") UUID pointId) {
        return ResponseEntity.ok(pointService.getPointById(pointId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(POINT_PATH)
    public ResponseEntity<Void> postPoint(@RequestBody PointDTO pointDTO) {
        PointDTO point = pointService.saveNewPoint(pointDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", POINT_PATH+ "/" + point.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(POINT_PATH_ID)
    public ResponseEntity<Void> putPointById(@PathVariable("pointId") UUID pointId, @RequestBody PointDTO pointDTO) {
        pointService.updatePointById(pointId, pointDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(POINT_PATH_ID)
    public ResponseEntity<Void> deletePointById(@PathVariable("pointId") UUID pointId) {
        pointService.deletePointById(pointId);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping(POINT_PATH_ID)
    public ResponseEntity<Void> patchPointById(@PathVariable("pointId") UUID pointId, @RequestBody PointDTO pointDTO) {
        pointService.patchPointById(pointId, pointDTO);

        return ResponseEntity.noContent().build();
    }

}
