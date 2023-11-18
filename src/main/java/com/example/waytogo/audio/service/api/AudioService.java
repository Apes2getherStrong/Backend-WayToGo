package com.example.waytogo.audio.service.api;

import com.example.waytogo.audio.model.dto.AudioDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface AudioService {
    Page<AudioDTO> getAllAudios(Integer pageNumber, Integer pageSize);

    Optional<AudioDTO> getAudioById(UUID audioId);

    AudioDTO saveNewAudio(AudioDTO audioDTO);

    AudioDTO updateUserById(UUID audioId, AudioDTO audioDTO);

    void deleteAudioById(UUID audioId);

    void patchAudioById(UUID audioId, AudioDTO audioDTO);
}
