package com.example.waytogo.initialize.csvLoading.service;

import com.example.waytogo.audio.model.csvModel.AudioCsvRecord;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.maplocation.model.csvModel.MapLocationCsvRecord;
import com.example.waytogo.route.model.csvModel.RoutesCsvRecord;
import com.example.waytogo.routes_maplocation.csvModel.RouteMapLocationCsvRecord;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvServiceLoader {
    private final JdbcTemplate jdbcTemplate;

    public void loadUsers(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<UserCsvRecord> usersCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, UserCsvRecord.class);

        String sql = "INSERT INTO USERS (user_id, username, password, login) VALUES(?, ?, ?, ?)";

        usersCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getUsername(), userCsvRecord.getPassword(), userCsvRecord.getLogin());
        });
    }

    public void loadMapLocations(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<MapLocationCsvRecord> mapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, MapLocationCsvRecord.class);

        String sql = "INSERT INTO map_locations (map_location_id, name, description, coordinates) VALUES(?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))";

        mapLocationsCsv.forEach(mapLocationCsvRecord -> {
            jdbcTemplate.update(sql, mapLocationCsvRecord.getId(), mapLocationCsvRecord.getName(), mapLocationCsvRecord.getDescription(), mapLocationCsvRecord.getCoord_x(), mapLocationCsvRecord.getCoord_y());
        });


    }

    public void loadAudios(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<AudioCsvRecord> audiosCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, AudioCsvRecord.class);

        String sql = "INSERT INTO audios (audio_id, audio_name, description, user_id, map_location_id) VALUES(?, ?, ?, ?, ?)";

        audiosCsv.forEach(audiosCsvRecord -> {
            jdbcTemplate.update(sql, audiosCsvRecord.getId(), audiosCsvRecord.getName(), audiosCsvRecord.getDescription(), audiosCsvRecord.getUser(), audiosCsvRecord.getMapLocation());
        });


    }

    public void loadRoutes(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<RoutesCsvRecord> routesCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, RoutesCsvRecord.class);

        String sql = "INSERT INTO routes (route_id, user_user_id, name, description) VALUES(?, ?, ?, ?)";

        routesCsv.forEach(routesCsvRecord -> {
            jdbcTemplate.update(sql, routesCsvRecord.getId(), routesCsvRecord.getUser(), routesCsvRecord.getName(), routesCsvRecord.getDescription());
        });


    }

    public void loadRoutesMapLocations(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<RouteMapLocationCsvRecord> routesMapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, RouteMapLocationCsvRecord.class);

        String sql = "INSERT INTO routes_map_locations (routes_map_location_id, map_location_id, route_id, sequence_nr) VALUES(?, ?, ?, ?)";

        routesMapLocationsCsv.forEach(routesMapLocationCsvRecord -> {
            jdbcTemplate.update(sql, routesMapLocationCsvRecord.getId(), routesMapLocationCsvRecord.getMapLocation(), routesMapLocationCsvRecord.getRoute(), routesMapLocationCsvRecord.getSequenceNr());
        });

    }


}
