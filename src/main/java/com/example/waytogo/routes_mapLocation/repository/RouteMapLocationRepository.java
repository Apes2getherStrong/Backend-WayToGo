package com.example.waytogo.routes_mapLocation.repository;

import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RouteMapLocationRepository extends JpaRepository<RouteMapLocation, UUID> {
    List<RouteMapLocation> getRouteMapLocationByRouteId(UUID id);

}