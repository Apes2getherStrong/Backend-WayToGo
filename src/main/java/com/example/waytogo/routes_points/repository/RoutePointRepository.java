package com.example.waytogo.routes_points.repository;

import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_points.entity.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutePointRepository  extends JpaRepository<RoutePoint, UUID> {
    List<RoutePoint> getRoutePointByRouteId(UUID id);

}