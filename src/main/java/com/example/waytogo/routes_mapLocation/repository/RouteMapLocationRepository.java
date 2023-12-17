package com.example.waytogo.routes_mapLocation.repository;

import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.routes_mapLocation.entity.RouteMapLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteMapLocationRepository extends JpaRepository<RouteMapLocation, UUID> {

    /***
     * SELECT ml.*
     * FROM map_locations ml
     * JOIN routes_map_locations rm ON ml.map_location_id = rm.map_location_id
     * JOIN routes r ON rm.route_id = r.route_id
     * WHERE r.route_id = '85db2153-8dd8-4ffd-8bd1-e3cc407f4ed0'::uuid;
     */
    @Query("SELECT rml.mapLocation FROM RouteMapLocation rml WHERE rml.route.id = :routeId")
    Page<MapLocation> findMapLocationsByRouteId(@Param("routeId") UUID routeId, Pageable pageable);
}