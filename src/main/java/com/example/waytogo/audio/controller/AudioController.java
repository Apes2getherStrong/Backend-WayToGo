package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.service.api.AudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    public static final String AUDIO_PATH = "/api/v1/audios";
    public static final String AUDIO_PATH_ID = AUDIO_PATH + "/{audioId}";
    public static final String USER_PATH_ID_AUDIOS = "/api/v1/users/{userId}/audios"; //Getting all audios from one user

    private final AudioService audioService;

    @GetMapping(AUDIO_PATH)
    public ResponseEntity<Page<AudioDTO>> getAllAudios(@RequestParam(required = false) Integer pageNumber,
                                                       @RequestParam(required = false) Integer pageSize) {
        Page<AudioDTO> audioDTOPage = audioService.getAllAudios(pageNumber, pageSize);
        return new ResponseEntity<>(audioDTOPage, HttpStatus.OK);
    }

    @GetMapping(AUDIO_PATH_ID)
    public ResponseEntity<AudioDTO> getAudioById(@PathVariable("audioId") UUID audioId) {
        return ResponseEntity.ok(audioService.getAudioById(audioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping(USER_PATH_ID_AUDIOS)
    public ResponseEntity<Page<AudioDTO>> getAllAudiosByUserId(@PathVariable("userId") UUID userId,
                                                               @RequestParam(required = false) Integer pageNumber,
                                                               @RequestParam(required = false) Integer pageSize) {
        Page<AudioDTO> audioDTOList = audioService.getAllAudiosByUserId(userId, pageNumber, pageSize);

        return new ResponseEntity<>(audioDTOList, HttpStatus.OK);
    }

    @PostMapping(AUDIO_PATH)
    public ResponseEntity<AudioDTO> postAudio(@Validated @RequestBody AudioDTO audioDTO) {
        AudioDTO savedAudio = audioService.saveNewAudio(audioDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", AUDIO_PATH + "/" + savedAudio.getId().toString());

        return new ResponseEntity<>(savedAudio, headers, HttpStatus.CREATED);
    }

    @PutMapping(AUDIO_PATH_ID)
    public ResponseEntity<AudioDTO> putAudioById(@PathVariable("audioId") UUID audioId,
                                                 @RequestBody AudioDTO audioDTO) {
        if (audioService.updateUserById(audioId, audioDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(AUDIO_PATH_ID)
    public ResponseEntity<Void> deleteAudioById(@PathVariable("audioId") UUID audioId) {
        if (!audioService.deleteAudioById(audioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(AUDIO_PATH_ID)
    public ResponseEntity<Void> patchAudioById(@PathVariable("audioId") UUID audioId, @RequestBody AudioDTO audioDTO) {
        if (audioService.patchAudioById(audioId, audioDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
