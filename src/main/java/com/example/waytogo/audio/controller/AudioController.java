package com.example.waytogo.audio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AudioController {

    private static final String AUDIO_PATH = "/api/audios";
    private static final String AUDIO_PATH_ID = AUDIO_PATH + "/{audioId}";

}
