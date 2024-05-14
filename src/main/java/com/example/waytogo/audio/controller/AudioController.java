package com.example.waytogo.audio.controller;

import com.example.waytogo.audio.model.dto.AudioDTO;
import com.example.waytogo.audio.service.api.AudioService;
import com.example.waytogo.maplocation.controller.MapLocationController;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AudioController {

    public static final String AUDIO_PATH = "/api/v1/audios";
    public static final String AUDIO_PATH_ID = AUDIO_PATH + "/{audioId}";
    public static final String USER_PATH_ID_AUDIOS = "/api/v1/users/{userId}/audios"; //Getting all audios from one user
    //TODO maybe (MapLocationController.MAP_LOCATION_PATH_ID + "/audios")?
    public static final String MAP_LOCATION_PATH_ID_AUDIOS = "/api/v1/mapLocations/{mapLocationId}/audios";

    private final AudioService audioService;

    public static final String AUDIO_PATH_ID_AUDIO = AUDIO_PATH_ID + "/audio";

    //TODO change the path (it shouldn't be hardcoded)
    public static final String AUDIO_DIRECTORY_PATH = "src/main/java/com/example/waytogo/audio/audio_files/";

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

    @GetMapping(MAP_LOCATION_PATH_ID_AUDIOS)
    public ResponseEntity<Page<AudioDTO>> getAllAudiosByMapLocationId(@PathVariable("mapLocationId") UUID mapLocationId,
                                                                      @RequestParam(required = false) Integer pageNumber,
                                                                      @RequestParam(required = false) Integer pageSize
    ) {
        Page<AudioDTO> audioDTOPage = audioService.getAllAudiosByMapLocationId(mapLocationId, pageNumber, pageSize);

        return new ResponseEntity<>(audioDTOPage, HttpStatus.OK);
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

        try {
            if (!audioService.deleteAudioById(audioId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping(AUDIO_PATH_ID)
    public ResponseEntity<Void> patchAudioById(@PathVariable("audioId") UUID audioId, @RequestBody AudioDTO audioDTO) {
        if (audioService.patchAudioById(audioId, audioDTO).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(AUDIO_PATH_ID_AUDIO)
    @ResponseBody
    public ResponseEntity<Void> postAudioFile(@PathVariable("audioId") UUID audioId,
                                              @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            if (isValidFileExtension(originalFilename)) {
                try {
                    if (audioService.saveNewAudioFile(file, audioId)) {
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    } else {
                        //audio not found
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the file", e);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidFileExtension(String fileName) {
        String[] allowedExtensions = {"mp3"};
        String fileExtension = StringUtils.getFilenameExtension(fileName);
        return Arrays.asList(allowedExtensions).contains(fileExtension.toLowerCase());
    }

    @GetMapping(AUDIO_PATH_ID_AUDIO)
    public ResponseEntity<byte[]> getAudioFile(@PathVariable("audioId") UUID audioId) {
        try {

            Optional<byte[]> audioBytesOpt = audioService.getAudioFileByAudioId(audioId);
            HttpHeaders headers = new HttpHeaders();

            if (audioBytesOpt.isEmpty()) {
                headers.add("Warning", "Audio (entity) not found");
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }

            byte[] audioBytes = audioBytesOpt.get();
            if (audioBytes.length == 0) {
                headers.add("Warning", "Audio (file) not found");
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }


            headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
            headers.setContentLength(audioBytes.length);
            //headers.setContentDispositionFormData("attachment", filename);
            return new ResponseEntity<>(audioBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
