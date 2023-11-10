package com.example.waytogo.route.mapper;

import com.example.waytogo.route.model.dto.GetRouteResponse;
import com.example.waytogo.route.model.dto.PostRouteRequest;
import com.example.waytogo.route.model.dto.PutRouteRequest;
import com.example.waytogo.route.model.dto.RouteDTO;
import com.example.waytogo.route.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper
public interface RouteMapper {
    GetRouteResponse routeToGetRouteResponse(Route route);
    Route postRouteRequestToRoute(PostRouteRequest route);
    Route putRouteRequestToRoute(PutRouteRequest route);
}
