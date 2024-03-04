package com.example.waytogo.audio.service.api;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface AudioService {
    Page<AudioDTO> getAllAudios(Integer pageNumber, Integer pageSize);

    Optional<AudioDTO> getAudioById(UUID audioId);

    Page<AudioDTO> getAllAudiosByUserId(UUID userId, Integer pageNumber, Integer pageSize);

    Page<AudioDTO> getAllAudiosByMapLocationId(UUID mapLocationId, Integer pageNumber, Integer pageSize);

    AudioDTO saveNewAudio(@Valid AudioDTO audioDTO);

    Optional<AudioDTO> updateUserById(UUID audioId, @Valid AudioDTO audioDTO);

    Boolean deleteAudioById(UUID audioId) throws IOException;

    Optional<AudioDTO> patchAudioById(UUID audioId, AudioDTO audioDTO);

    void setUserToNullByUserId(UUID userId);

    Boolean saveNewAudioFile(MultipartFile file, UUID audioId) throws IOException;

    void deleteAudioFile(Audio audio) throws IOException;

    void deleteAudioByMapLocationId(UUID mapLocationId) throws IOException;

    Optional<byte[]> getAudioFileByAudioId(UUID audioId) throws IOException;
}
