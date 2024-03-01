package com.example.waytogo.audio.service.impl;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.user.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@RequiredArgsConstructor
@Service
@Validated
public class AudioServiceJPA implements AudioService {
    private final AudioMapper audioMapper;
    private final AudioRepository audioRepository;

    private final MapLocationMapper mapLocationMapper;
    private final UserMapper userMapper;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<AudioDTO> getAllAudios(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Audio> audioPage;
        audioPage = audioRepository.findAll(pageRequest);

        return audioPage.map(audioMapper::audioToAudioDto);
    }

    @Override
    public Optional<AudioDTO> getAudioById(UUID audioId) {
        return Optional.ofNullable(audioMapper.audioToAudioDto(audioRepository.findById(audioId)
                .orElse(null)));
    }

    @Override
    public Page<AudioDTO> getAllAudiosByUserId(UUID userId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Audio> audioPage;
        audioPage = audioRepository.findByUser_Id(userId, pageRequest);

        return audioPage.map(audioMapper::audioToAudioDto);
    }

    @Override
    public Page<AudioDTO> getAllAudiosByMapLocationId(UUID mapLocationId, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Audio> audioPage;
        audioPage = audioRepository.findByMapLocation_Id(mapLocationId, pageRequest);

        return audioPage.map(audioMapper::audioToAudioDto);
    }


    @Override
    public AudioDTO saveNewAudio(@Valid AudioDTO audioDTO) {
        return audioMapper.audioToAudioDto(audioRepository.save(audioMapper.audioDtoToAudio(audioDTO)));
    }

    @Override
    public Optional<AudioDTO> updateUserById(UUID audioId, @Valid AudioDTO audioDTO) {
        AtomicReference<Optional<AudioDTO>> atomicReference = new AtomicReference<>();

        audioRepository.findById(audioId).ifPresentOrElse(found -> {
            audioDTO.setId(audioId);
            atomicReference.set(Optional.of(audioMapper
                    .audioToAudioDto(audioRepository
                            .save(audioMapper.audioDtoToAudio(audioDTO)))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteAudioById(UUID audioId) {
        if (audioRepository.existsById(audioId)) {
            audioRepository.deleteById(audioId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<AudioDTO> patchAudioById(UUID audioId, AudioDTO audioDTO) {
        AtomicReference<Optional<AudioDTO>> atomicReference = new AtomicReference<>();

        audioRepository.findById(audioId).ifPresentOrElse(foundAudio -> {
            if (StringUtils.hasText(audioDTO.getName())) {
                foundAudio.setName(audioDTO.getName());
            }
            if (audioDTO.getMapLocation() != null) {
                foundAudio.setMapLocation(mapLocationMapper.mapLocationDtoToMapLocation(audioDTO.getMapLocation()));
            }
            if (audioDTO.getUser() != null) {
                foundAudio.setUser(userMapper.userDtoToUser(audioDTO.getUser()));
            }
            if (audioDTO.getUser() != null) {
                foundAudio.setUser(userMapper.userDtoToUser(audioDTO.getUser()));
            }
            atomicReference.set(Optional.of(audioMapper.audioToAudioDto(audioRepository.save(foundAudio))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("name"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
