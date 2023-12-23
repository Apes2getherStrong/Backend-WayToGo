package com.example.waytogo.route.repository;

import com.example.waytogo.route.model.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface RouteRepository  extends JpaRepository<Route, UUID> {

    Page<Route> findByUser_Id(UUID userId, PageRequest pageRequest);


    //dlaczego nie działa?
    @Modifying
    @Transactional
    @Query("UPDATE Route r SET r.user = null WHERE r.user.id = :userId")
    int setUserToNullByUserId(@Param("userId") UUID userId);

}
