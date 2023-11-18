package com.example.waytogo.audio.service.impl;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AudioServiceJPA implements AudioService {
    AudioMapper audioMapper;
    AudioRepository audioRepository;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<AudioDTO> getAllAudios(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber,pageSize);

        Page<Audio> audioPage;
        audioPage = audioRepository.findAll(pageRequest);

        return audioPage.map(audioMapper::audioToAudioDto);
    }

    @Override
    public Optional<AudioDTO> getAudioById(UUID audioId) {
        return Optional.ofNullable(audioMapper.audioToAudioDto(audioRepository.findById(audioId)
                .orElse(null)));
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

        Sort sort = Sort.by(Sort.Order.asc("audio_name"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
