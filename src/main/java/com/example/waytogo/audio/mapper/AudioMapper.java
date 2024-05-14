package com.example.waytogo.audio.mapper;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.model.entity.Audio;
import org.mapstruct.Mapper;

@Mapper
public interface AudioMapper {
    Audio audioDtoToAudio(AudioDTO dto);

    AudioDTO audioToAudioDto(Audio audio);
}