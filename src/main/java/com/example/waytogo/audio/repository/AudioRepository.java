package com.example.waytogo.audio.repository;

import com.example.waytogo.audio.model.entity.Audio;
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
public interface AudioRepository extends JpaRepository<Audio, UUID> {
    Page<Audio> findByUser_Id(UUID uuid, PageRequest pageRequest);
    Page<Audio> findByMapLocation_Id(UUID id, PageRequest pageRequest);

    @Modifying
    @Transactional
    @Query("UPDATE Audio a SET a.user = null WHERE a.user.id = :userId")
    int setUserToNullByUserId(@Param("userId") UUID userId);
}
