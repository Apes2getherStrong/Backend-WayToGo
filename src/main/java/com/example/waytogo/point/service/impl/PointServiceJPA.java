package com.example.waytogo.point.service.impl;

import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.point.service.api.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class PointServiceJPA implements PointService {
    private final PointMapper pointMapper;
    private final PointRepository pointRepository;
    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @Override
    public PointDTO saveNewPoint(PointDTO point) {
        return pointMapper.pointToPointDto(pointRepository.save(pointMapper.pointDtoToPoint(point)));
    }

    @Override
    public Optional<PointDTO> getPointById(UUID pointId) {
        return pointRepository.findById(pointId).map(pointMapper::pointToPointDto);
    }


    /***
     * Delete point by id if exists and return True, if not exists return False
     * @param pointId
     * @return Boolean
     */
    @Override
    public Boolean deletePointById(UUID pointId) {
        if (pointRepository.existsById(pointId)) {
            pointRepository.deleteById(pointId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<PointDTO> updatePointById(UUID pointId,PointDTO pointDTO) {
        AtomicReference<Optional<PointDTO>> atomicReference = new AtomicReference<>();

        pointRepository.findById(pointId).ifPresentOrElse(found -> {
            PointDTO foundDTO = pointMapper.pointToPointDto(found);
            foundDTO.setName(pointDTO.getName());
            foundDTO.setCoordinates(pointDTO.getCoordinates());

            atomicReference.set(Optional.of(pointMapper
                    .pointToPointDto(pointRepository
                            .save(pointMapper.pointDtoToPoint(foundDTO)))));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Optional<PointDTO> patchPointById(UUID pointId, PointDTO pointDTO) {
        AtomicReference<Optional<PointDTO>> atomicReference = new AtomicReference<>();

        pointRepository.findById(pointId).ifPresentOrElse(point -> {
            if (StringUtils.hasText(pointDTO.getName())) {
                point.setName(pointDTO.getName());

            }
            if (pointDTO.getCoordinates() != null) {
                if (pointDTO.getCoordinates().getLatitude() != null) {
                    point.getCoordinates().setLatitude(pointDTO.getCoordinates().getLatitude());
                }
                if (pointDTO.getCoordinates().getLongitude() != null) {
                    point.getCoordinates().setLongitude(pointDTO.getCoordinates().getLongitude());
                }
            }
            atomicReference.set(Optional.of(pointMapper.pointToPointDto(pointRepository.save(point))));

        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
    @Override
    public Page<PointDTO> getAllPoints(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Point> pointPage = pointRepository.findAll(pageRequest);

        return pointPage.map(pointMapper::pointToPointDto);
    }
    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = DEFAULT_PAGE;
        int queryPageSize = DEFAULT_PAGE_SIZE;
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        }
        if (pageSize != null && pageSize > 0) {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
