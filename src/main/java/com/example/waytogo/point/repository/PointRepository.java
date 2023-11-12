package com.example.waytogo.point.repository;

import com.example.waytogo.point.model.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {

}
