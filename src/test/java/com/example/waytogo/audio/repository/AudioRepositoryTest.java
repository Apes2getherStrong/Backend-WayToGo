package com.example.waytogo.audio.repository;

import com.example.waytogo.audio.controller.AudioController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AudioController.class})
class AudioRepositoryTest {



}