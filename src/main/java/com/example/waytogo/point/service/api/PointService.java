package com.example.waytogo.point.service.api;

import com.example.waytogo.point.model.dto.PointDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PointService {
    PointDTO saveNewPoint(PointDTO point);

    Optional<PointDTO> getPointById(UUID pointId);

    Page<PointDTO> getAllPoints(Integer pageNumber, Integer pageSize);

    void deletePointById(UUID pointId);

    void updatePointById(UUID pointId, PointDTO pointDTO);

    void patchPointById(UUID pointId, PointDTO pointDTO);
}
