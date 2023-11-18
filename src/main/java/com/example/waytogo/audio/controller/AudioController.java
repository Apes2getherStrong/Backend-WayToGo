package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.service.api.AudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    private static final String AUDIO_PATH = "/api/audios";
    private static final String AUDIO_PATH_ID = AUDIO_PATH + "/{audioId}";

    AudioService audioService;

    @GetMapping(AUDIO_PATH)
    public Page<AudioDTO> getAllAudios(@RequestParam(required = false) Integer pageNumber,
                                       @RequestParam(required = false) Integer pageSize) {
        return audioService.getAllAudios(pageNumber,pageSize);
    }

    @GetMapping(AUDIO_PATH_ID)
    public ResponseEntity<AudioDTO> getAudioById(@PathVariable("audioId")UUID audioId) {
        return ResponseEntity.ok(audioService.getAudioById(audioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(AUDIO_PATH)
    public ResponseEntity<AudioDTO> postAudio(@RequestBody AudioDTO audioDTO) {
        AudioDTO savedAudio  = audioService.saveNewAudio(audioDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", AUDIO_PATH + savedAudio.getId().toString());

        return new ResponseEntity<>(savedAudio,headers, HttpStatus.CREATED);
    }

    @PutMapping(AUDIO_PATH_ID)
    public ResponseEntity<AudioDTO> putAudioById(@PathVariable("audioId") UUID audioId,
                                                 @RequestBody AudioDTO audioDTO) {
        AudioDTO updatedAudio = audioService.updateUserById(audioId, audioDTO);

        return new ResponseEntity<>(updatedAudio, HttpStatus.CREATED);
    }

}
