package com.example.waytogo.audio.service.impl;

import com.example.waytogo.audio.controller.AudioController;
import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.mapper.MapLocationMapper;
import com.example.waytogo.route.controller.RouteController;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.user.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@RequiredArgsConstructor
@Service
@Validated
public class AudioServiceJPA implements AudioService {
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;
    private final AudioMapper audioMapper;
    private final AudioRepository audioRepository;
    private final MapLocationMapper mapLocationMapper;
    private final UserMapper userMapper;

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
    public Boolean deleteAudioById(UUID audioId) throws IOException {
        Optional<Audio> optAudio = audioRepository.findById(audioId);
        if(optAudio.isEmpty()) {
            return false;
        }
        else {
            Audio a = optAudio.get();
            audioRepository.deleteById(audioId);
            return true;
        }

    }

    @Override
    public Optional<AudioDTO> patchAudioById(UUID audioId,@Valid AudioDTO audioDTO) {
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

    @Transactional
    @Override
    public void setUserToNullByUserId(UUID userId) {
        for (Audio a : audioRepository.findByUser_Id(userId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
            a.setUser(null);
        }
    }


    //TODO check ifaudioRepository.save(audio); is necessary
    //TODO write tests

    @Override
    public Boolean saveNewAudioFile(MultipartFile file, UUID audioId) throws IOException {
        byte[] bytes = file.getBytes();

        Optional<Audio> optAudio = audioRepository.findById(audioId);
        if (optAudio.isEmpty()) {
            return false;
        }
        Audio audio = optAudio.get();

        audio.setAudioData(bytes);
        audioRepository.save(audio);

        return true;
    }

    @Override
    public void deleteAudioByMapLocationId(UUID mapLocationId) throws IOException {
        for (Audio a : audioRepository.findByMapLocation_Id(mapLocationId, PageRequest.of(0, Integer.MAX_VALUE)).getContent()) {
            audioRepository.deleteById(a.getId());
        }
    }


    //TODO weird function output. Instead of looking at the output of the function, exception handling or
    //TODO controler advisor can be implemented. Disadvantage: exceptions are slower

    //empty optional when audio(entity) not found, empty array when audito(entity) found but audio file not found
    @Override
    public Optional<byte[]> getAudioFileByAudioId(UUID audioId) throws IOException {
        Optional<Audio> optAudio = audioRepository.findById(audioId);
        if (optAudio.isEmpty()) {
            return Optional.empty();
        }

        byte[] audioData = optAudio.get().getAudioData();
        return Optional.ofNullable(audioData);
    }
}
