package com.example.waytogo.initialize;

import com.example.waytogo.point.model.entity.Coordinates;
import com.example.waytogo.point.model.entity.Point;
import com.example.waytogo.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class initializationBasic implements InitializingBean {
    private final PointRepository pointRepository;
    @Override
    public void afterPropertiesSet() throws Exception {
        Point point = Point.builder()
                .coordinates(Coordinates.builder()
                        .latitude(12.0)
                        .longitude(13.0).build())
                .name("skurwysyn posejdon")
                .build();
        log.debug("saving point: {}", point);
        pointRepository.save(point);
    }
}
