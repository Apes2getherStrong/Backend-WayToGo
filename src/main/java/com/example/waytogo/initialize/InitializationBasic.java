package com.example.waytogo.initialize;

import com.example.waytogo.initialize.csvLoading.service.CsvServiceLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final CsvServiceLoader csvServiceLoader;

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        csvServiceLoader.loadUsers("classpath:csvData/users.csv");
        csvServiceLoader.loadRoutes("classpath:csvData/routes.csv");
        csvServiceLoader.loadMapLocations("classpath:csvData/map_locations.csv");
        csvServiceLoader.loadAudios("classpath:csvData/audios.csv");
        csvServiceLoader.loadRoutesMapLocations("classpath:csvData/route_map_locations.csv");
    }

}
