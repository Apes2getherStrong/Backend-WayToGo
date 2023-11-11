package com.example.waytogo.point.service.api;

import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import org.springframework.stereotype.Service;

public interface PointService {
    PointDTO saveNewPoint(PointDTO point);
}
