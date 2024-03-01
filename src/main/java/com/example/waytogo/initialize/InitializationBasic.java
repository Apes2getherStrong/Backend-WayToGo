package com.example.waytogo.initialize;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.initialize.csvLoading.service.CsvServiceLoader;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final CsvServiceLoader csvServiceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        csvServiceLoader.loadUsers("classpath:csvData/users.csv");
        csvServiceLoader.loadMapLocations("classpath:csvData/map_locations.csv");
        csvServiceLoader.loadAudios("classpath:csvData/audios.csv");
        csvServiceLoader.loadRoutes("classpath:csvData/routes.csv");
        csvServiceLoader.loadRoutes("classpath:csvData/route_map_locations.csv");
    }

}
