package com.example.waytogo.audio.repository;

import com.example.waytogo.audio.model.entity.Audio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AudioRepository extends JpaRepository<Audio, UUID> {
    //Page<Audio> findByUser_UserId(UUID userId);
    //Page<Audio> findByUser_UserId(UUID uuid, PageRequest pageRequest);
    Page<Audio> findByUser_Id(UUID uuid, PageRequest pageRequest);
}
