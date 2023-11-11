package com.example.waytogo.route.mapper;

import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper
public interface RouteMapper {

    RouteDTO routeToRouteDto(Route route);
    Route routeDtoToRoute(RouteDTO routeDTO);
}
