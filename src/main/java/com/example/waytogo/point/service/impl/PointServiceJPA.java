package com.example.waytogo.point.service.impl;

import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.point.service.api.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class PointServiceJPA implements PointService {
    private final PointMapper pointMapper;
    private final PointRepository pointRepository;
    @Override
    public PointDTO saveNewPoint(PointDTO point) {
        return  pointMapper.pointToPointDto(pointRepository.save(pointMapper.pointDtoToPoint(point)));
    }
}
