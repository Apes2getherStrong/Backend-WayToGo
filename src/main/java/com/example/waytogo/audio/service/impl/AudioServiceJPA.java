package com.example.waytogo.audio.service.impl;

import com.example.waytogo.audio.mapper.AudioMapper;
import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.point.mapper.PointMapper;
import com.example.waytogo.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AudioServiceJPA implements AudioService {
    AudioMapper audioMapper;
    AudioRepository audioRepository;

    PointMapper pointMapper;
    UserMapper userMapper;

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

    @Override
    public List<AudioDTO> getAllAudiosByUserId(UUID userId) {
        return audioRepository.findByUser_UserId(userId).stream()
                .map(audioMapper::audioToAudioDto)
                .collect(Collectors.toList());
    }


    @Override
    public AudioDTO saveNewAudio(AudioDTO audioDTO) {
        return audioMapper.audioToAudioDto(audioRepository.save(audioMapper.audioDtoToAudio(audioDTO)));
    }

    @Override
    public AudioDTO updateUserById(UUID audioId, AudioDTO audioDTO) {
        audioDTO.setId(audioId);
        return audioMapper.audioToAudioDto(audioRepository.save(audioMapper.audioDtoToAudio(audioDTO)));
    }

    @Override
    public void deleteAudioById(UUID audioId) {
        audioRepository.deleteById(audioId);
    }

    @Override
    public void patchAudioById(UUID audioId, AudioDTO audioDTO) {

        audioRepository.findById(audioId).ifPresent(foundAudio -> {
            if (StringUtils.hasText(audioDTO.getName())) {
                foundAudio.setName(audioDTO.getName());
            }
            if (audioDTO.getPoint() != null) {
                foundAudio.setPoint(pointMapper.pointDtoToPoint(audioDTO.getPoint()));
            }
            if(audioDTO.getUser() != null) {
                foundAudio.setUser(userMapper.userDtoToUser(audioDTO.getUser()));
            }

        });
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
