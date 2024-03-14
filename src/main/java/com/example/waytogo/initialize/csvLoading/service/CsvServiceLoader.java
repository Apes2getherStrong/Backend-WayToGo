package com.example.waytogo.initialize.csvLoading.service;

import com.example.waytogo.audio.model.csvModel.AudioCsvRecord;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.maplocation.model.csvModel.MapLocationCsvRecord;
import com.example.waytogo.route.model.csvModel.RoutesCsvRecord;
import com.example.waytogo.routes_maplocation.csvModel.RouteMapLocationCsvRecord;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvServiceLoader {
    private final JdbcTemplate jdbcTemplate;

    public void loadUsers(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();

        List<UserCsvRecord> usersCsv = CsvConverterGeneric.convertCsvFileToCsvModel(inputStream, UserCsvRecord.class);

        String sql = "INSERT INTO USERS (user_id, username, password, login) VALUES(?, ?, ?, ?)";

        usersCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getUsername(), userCsvRecord.getPassword(), userCsvRecord.getLogin());
        });
    }

    public void loadMapLocations(Resource resource) throws IOException, SQLException {
        InputStream inputStream = resource.getInputStream();

        List<MapLocationCsvRecord> mapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(inputStream, MapLocationCsvRecord.class);

        String sql = "INSERT INTO map_locations (map_location_id, name, description, coordinates) VALUES(?, ?, ?, 'POINT(' || ? || ' ' || ? || ')')";

        mapLocationsCsv.forEach(mapLocationCsvRecord -> {
            jdbcTemplate.update(sql, mapLocationCsvRecord.getId(), mapLocationCsvRecord.getName(), mapLocationCsvRecord.getDescription(), mapLocationCsvRecord.getCoord_x(), mapLocationCsvRecord.getCoord_y());
        });

    }

    public void loadAudios(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();

        List<AudioCsvRecord> audiosCsv = CsvConverterGeneric.convertCsvFileToCsvModel(inputStream, AudioCsvRecord.class);

        String sql = "INSERT INTO audios (audio_id, audio_name, description, user_id, map_location_id) VALUES(?, ?, ?, ?, ?)";

        audiosCsv.forEach(audiosCsvRecord -> {
            jdbcTemplate.update(sql, audiosCsvRecord.getId(), audiosCsvRecord.getName(), audiosCsvRecord.getDescription(), audiosCsvRecord.getUser(), audiosCsvRecord.getMapLocation());
        });


    }

    public void loadRoutes(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();

        List<RoutesCsvRecord> routesCsv = CsvConverterGeneric.convertCsvFileToCsvModel(inputStream, RoutesCsvRecord.class);

        String sql = "INSERT INTO routes (route_id, user_id, name, description) VALUES(?, ?, ?, ?)";

        routesCsv.forEach(routesCsvRecord -> {
            jdbcTemplate.update(sql, routesCsvRecord.getId(), routesCsvRecord.getUser(), routesCsvRecord.getName(), routesCsvRecord.getDescription());
        });


    }

    public void loadRoutesMapLocations(Resource resource) throws IOException {
        InputStream inputStream = resource.getInputStream();

        List<RouteMapLocationCsvRecord> routesMapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(inputStream, RouteMapLocationCsvRecord.class);

        String sql = "INSERT INTO routes_map_locations (routes_map_location_id, map_location_id, route_id, sequence_nr) VALUES(?, ?, ?, ?)";

        routesMapLocationsCsv.forEach(routesMapLocationCsvRecord -> {
            jdbcTemplate.update(sql, routesMapLocationCsvRecord.getId(), routesMapLocationCsvRecord.getMapLocation(), routesMapLocationCsvRecord.getRoute(), routesMapLocationCsvRecord.getSequenceNr());
        });

    }


}
