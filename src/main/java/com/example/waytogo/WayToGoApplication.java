package com.example.waytogo;

import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WayToGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WayToGoApplication.class, args);
    }

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory();
    }


}
