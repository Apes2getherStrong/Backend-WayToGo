package com.example.waytogo.initialize;

import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.point.model.entity.Coordinates;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_points.entity.RoutePoint;
import com.example.waytogo.routes_points.repository.RoutePointRepository;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final PointRepository pointRepository;
    private final RoutePointRepository routePointRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;
    private final AudioRepository audioRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        Point point = Point.builder()
                .coordinates(Coordinates.builder()
                        .latitude(12.0)
                        .longitude(13.0).build())
                .name("skurwysyn posejdon")
                .build();
        pointRepository.save(point);

        Point point2 = Point.builder()
                .coordinates(Coordinates.builder()
                        .latitude(10.0)
                        .longitude(19.0).build())
                .name("gmach weti")
                .build();
        pointRepository.save(point2);

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
                .build();
        routeRepository.save(route1);

        List<User> users = userRepository.findAll();
        /*System.out.println(users.get(0).getAudios().get(0).getUuid());
        System.out.println(users.get(0).getRoutes().get(0).getId());*/
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
                .build();
        routeRepository.save(r1);

        Point p1 = Point.builder()
                .coordinates(Coordinates.builder()
                        .latitude(10.0)
                        .longitude(19.0).build())
                .name("p1")
                .build();
        pointRepository.save(p1);

        RoutePoint rp1 = RoutePoint.builder()
                .sequenceNr(1)
                .point(p1)
                .route(r1)
                .build();
        routePointRepository.save(rp1);



    }
}
