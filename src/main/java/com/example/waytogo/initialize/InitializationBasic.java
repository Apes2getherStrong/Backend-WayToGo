package com.example.waytogo.initialize;

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

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializationBasic implements InitializingBean {
    private final PointRepository pointRepository;
    private final RoutePointRepository routePointRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

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
    }

    private void tuSieBawiOskar() {
        User u1 = User.builder()
                .username("u1")
                .build();
        userRepository.save(u1);

        Route r1 = Route.builder()
                .user(u1)
                .name("r1")
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
