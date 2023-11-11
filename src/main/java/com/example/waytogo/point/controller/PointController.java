package com.example.waytogo.point.controller;

import com.example.waytogo.point.service.api.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController {
    private final static String POINT_PATH = "/api/v1/points";
    private final PointService pointService;

    @GetMapping(POINT_PATH)
    public ResponseEntity<Void> getAllPoints() {
        return ResponseEntity.ok().build();
    }
}
