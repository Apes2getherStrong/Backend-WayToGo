package com.example.waytogo.point.controller;

import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.service.api.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController {
    private final static String POINT_PATH = "/api/v1/points";
    private final static String POINT_PATH_ID = POINT_PATH + "/{pointId}";
    private final PointService pointService;

    @GetMapping(POINT_PATH) // dodac parametry
    public ResponseEntity<Page<PointDTO>> getAllPoints() {
        return null;
    }
    @GetMapping(POINT_PATH_ID)
    public ResponseEntity<Void> getPointById(@PathVariable("pointId") UUID pointId) {
        return null;
    }

    @PostMapping(POINT_PATH)
    public ResponseEntity<Void> postPoint(@RequestBody PointDTO pointDTO) {
        return null;
    }

    @PutMapping(POINT_PATH_ID)
    public ResponseEntity<Void> putPoint(@PathVariable("pointId") UUID pointId, @RequestBody PointDTO pointDTO) {
        return null;
    }

    @DeleteMapping(POINT_PATH_ID)
    public ResponseEntity<Void> deletePoint(@PathVariable("pointId") UUID pointId) {
        return null;
    }
    @PatchMapping(POINT_PATH_ID)
    public ResponseEntity<Void> patchPoint(@PathVariable("pointId") UUID pointId) {
        return null;
    }

}
