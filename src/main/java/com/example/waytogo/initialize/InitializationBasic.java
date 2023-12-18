package com.example.waytogo.initialize;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
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

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final MapLocationRepository mapLocationRepository;
    private final RouteMapLocationRepository routeMapLocationRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final AudioRepository audioRepository;
    private final GeometryFactory geometryFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        MapLocation mapLocation = MapLocation.builder()
                .name("skurwysyn posejdon")
                .coordinates(geometryFactory.createPoint(new Coordinate(10.2,32.1)))
                .build();
        mapLocationRepository.save(mapLocation);
        System.out.println(mapLocationRepository.findAll().get(0).getCoordinates());

        MapLocation mapLocation2 = MapLocation.builder()
                .name("gmach weti")
                .coordinates(geometryFactory.createPoint(new Coordinate(12.1,52.1)))
                .build();
        mapLocationRepository.save(mapLocation2);

        tuSieBawiOskar();
        hejkaTuLenka();
    }

    private void hejkaTuLenka() {
        User user1 = User.builder()
                .username("KUBALSON")
                .login("Mareczek")
                .password("KochamGrafy123!")
                .build();
        userRepository.save(user1);

        Audio audio1 = Audio.builder()
                .name("Wyklad 1")
                .user(user1)
                .build();
        audioRepository.save(audio1);

        Route route1 = Route.builder()
                .name("Dijkstra")
                .user(user1)
                .description("description")
                .build();
        routeRepository.save(route1);

        List<User> users = userRepository.findAll();
    }

    private void tuSieBawiOskar() {
        User u1 = User.builder()
                .username("u1")
                .password("p1")
                .login("l1")
                .build();
        userRepository.save(u1);

        Route r1 = Route.builder()
                .user(u1)
                .name("route1")
                .description("description")
                .build();
        routeRepository.save(r1);

        MapLocation p1 = MapLocation.builder()
                .coordinates(geometryFactory.createPoint(new Coordinate(5.2,11.33)))
                .name("p1")
                .build();
        mapLocationRepository.save(p1);

        RouteMapLocation rp1 = RouteMapLocation.builder()
                .sequenceNr(1)
                .mapLocation(p1)
                .route(r1)
                .build();
        routeMapLocationRepository.save(rp1);



    }
}
