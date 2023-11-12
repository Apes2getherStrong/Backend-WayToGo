package com.example.waytogo.point.mapper;

import com.example.waytogo.point.model.dto.PointDTO;
import com.example.waytogo.point.model.entity.Point;
import org.mapstruct.Mapper;

@Mapper
public interface PointMapper {

    Point pointDtoToPoint(PointDTO point);

    PointDTO pointToPointDto(Point point);
}
