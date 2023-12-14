package com.example.waytogo.audio.service.api;

import com.example.waytogo.audio.model.dto.AudioDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface AudioService {
    Page<AudioDTO> getAllAudios(Integer pageNumber, Integer pageSize);

    Optional<AudioDTO> getAudioById(UUID audioId);

    Page<AudioDTO> getAllAudiosByUserId(UUID userId, Integer pageNumber, Integer pageSize);

    AudioDTO saveNewAudio(@Valid AudioDTO audioDTO);

    Optional<AudioDTO> updateUserById(UUID audioId, @Valid AudioDTO audioDTO);

    boolean deleteAudioById(UUID audioId);

    Optional<AudioDTO> patchAudioById(UUID audioId, AudioDTO audioDTO);
}
