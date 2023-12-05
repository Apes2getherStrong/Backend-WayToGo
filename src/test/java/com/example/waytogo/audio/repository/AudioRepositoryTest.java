package com.example.waytogo.audio.repository;

import com.example.waytogo.audio.controller.AudioController;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import({AudioController.class})
class AudioRepositoryTest {



}