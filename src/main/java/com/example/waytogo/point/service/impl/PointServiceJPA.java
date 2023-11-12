package com.example.waytogo.point.service.impl;

import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.point.service.api.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class PointServiceJPA implements PointService {
    private final PointMapper pointMapper;
    private final PointRepository pointRepository;
    private static final Integer DEFAULT_PAGE =   0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    @Override
    public PointDTO saveNewPoint(PointDTO point) {
        return  pointMapper.pointToPointDto(pointRepository.save(pointMapper.pointDtoToPoint(point)));
    }

    @Override
    public Optional<PointDTO> getPointById(UUID pointId) {
        return pointRepository.findById(pointId).map(pointMapper::pointToPointDto);
    }

    @Override
    public Page<PointDTO> getAllPoints(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Point> pointPage = pointRepository.findAll(pageRequest);

        return pointPage.map(pointMapper::pointToPointDto);
    }

    @Override
    public void deletePointById(UUID pointId) {
        pointRepository.deleteById(pointId);
    }

    @Override
    public void updatePointById(UUID pointId, PointDTO pointDTO) {
        Point pointMapped = pointMapper.pointDtoToPoint(pointDTO);

        pointRepository.findById(pointId).ifPresent(point -> {
            point.setName(pointMapped.getName());
            point.setCoordinates(pointMapped.getCoordinates());
            pointRepository.save(point);
        });
    }

    @Override
    public void patchPointById(UUID pointId, PointDTO pointDTO) {
        pointRepository.findById(pointId).ifPresent(point -> {
            if (StringUtils.hasText(pointDTO.getName())){
                point.setName(pointDTO.getName());

            } if (pointDTO.getCoordinates() != null){
                if (pointDTO.getCoordinates().getLatitude() != null){
                    point.getCoordinates().setLatitude(pointDTO.getCoordinates().getLatitude());
                }
                if (pointDTO.getCoordinates().getLongitude() != null){
                    point.getCoordinates().setLongitude(pointDTO.getCoordinates().getLongitude());
                }
            }
            pointRepository.save(point);
        });
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = DEFAULT_PAGE;
        int queryPageSize = DEFAULT_PAGE_SIZE;
        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber -1;
        }
        if (pageSize != null && pageSize > 0) {
            if ( pageSize > 1000){
                queryPageSize = 1000;
            }else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
