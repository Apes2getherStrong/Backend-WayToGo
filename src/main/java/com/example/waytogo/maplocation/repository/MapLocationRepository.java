package com.example.waytogo.maplocation.repository;

import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.dto.RouteDTO;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MapLocationRepository extends JpaRepository<MapLocation, UUID> {
    @Query("SELECT m FROM MapLocation m WHERE ST_DistanceSphere(m.coordinates, :location) <= :range")
    Page<MapLocation> findByCoordinatesNear(@Param("location") Point location, @Param("range") Double range, Pageable pageable);
}
