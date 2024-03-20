package com.example.waytogo.routes_maplocation.mapper;

import com.example.waytogo.routes_maplocation.model.dto.RouteMapLocationDTO;
import com.example.waytogo.routes_maplocation.model.entity.RouteMapLocation;
import org.mapstruct.Mapper;

@Mapper
public interface RouteMapLocationMapper {
    RouteMapLocationDTO routeMapLocationToRouteMapLocationDto(RouteMapLocation routeMapLocation);
    RouteMapLocation routeMapLocationDtoToRouteMapLocation(RouteMapLocationDTO routeMapLocationDTO);
}
