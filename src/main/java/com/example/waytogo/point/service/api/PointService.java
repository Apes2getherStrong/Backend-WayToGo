package com.example.waytogo.point.service.api;

import com.example.waytogo.point.model.dto.PointDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
public interface PointService {
    PointDTO saveNewPoint(@Valid PointDTO point);

    Optional<PointDTO> getPointById(UUID pointId);

    Page<PointDTO> getAllPoints(Integer pageNumber, Integer pageSize);

    Boolean deletePointById(UUID pointId);

    Optional<PointDTO> updatePointById(UUID pointId, PointDTO pointDTO);

    Optional<PointDTO> patchPointById(UUID pointId, PointDTO pointDTO);
}
