package com.example.waytogo.maplocation.repository;

import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.dto.RouteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MapLocationRepository extends JpaRepository<MapLocation, UUID> {
}
