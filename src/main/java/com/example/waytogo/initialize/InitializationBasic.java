package com.example.waytogo.initialize;

import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.initialize.csvLoading.service.CsvServiceLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final CsvServiceLoader csvServiceLoader;
    private final AudioRepository audioRepository;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        if (audioRepository.count() < 20) {
            csvServiceLoader.loadUsers(new ClassPathResource("/csvData/users.csv"));
            csvServiceLoader.loadRoutes(new ClassPathResource("/csvData/routes.csv"));
            csvServiceLoader.loadMapLocations(new ClassPathResource("/csvData/map_locations.csv"));
            csvServiceLoader.loadAudios(new ClassPathResource("/csvData/audios.csv"));
            csvServiceLoader.loadRoutesMapLocations(new ClassPathResource("/csvData/route_map_locations.csv"));
        }
    }
}
