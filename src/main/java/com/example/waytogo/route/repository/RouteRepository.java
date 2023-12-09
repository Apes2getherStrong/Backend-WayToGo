package com.example.waytogo.route.repository;

import com.example.waytogo.route.model.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteRepository  extends JpaRepository<Route, UUID> {

    Page<Route> findByUser_Id(UUID userId, PageRequest pageRequest);
}
