package com.example.waytogo.initialize.csvLoading.service;

import com.example.waytogo.audio.model.csvModel.AudioCsvRecord;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.maplocation.model.csvModel.MapLocationCsvRecord;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.route.model.csvModel.RoutesCsvRecord;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.csvModel.RouteMapLocationCsvRecord;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import com.example.waytogo.user.model.entity.User;
import com.example.waytogo.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvServiceLoader {
    private final MapLocationRepository mapLocationRepository;
    private final RouteMapLocationRepository routeMapLocationRepository;
    private final RouteRepository routeRepository;
    private final AudioRepository audioRepository;
    private final GeometryFactory geometryFactory;
    private final UserRepository userRepository;
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

        mapLocationsCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getName(), userCsvRecord.getDescription(), userCsvRecord.getCoord_x(), userCsvRecord.getCoord_y());
        });


    }

    public void loadAudios(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<AudioCsvRecord> audiosCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, AudioCsvRecord.class);

        String sql = "INSERT INTO audios (audio_id, audio_name, description, user_id, map_location_id) VALUES(?, ?, ?, ?, ?)";

        audiosCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getName(), userCsvRecord.getDescription(), userCsvRecord.getUser(),userCsvRecord.getMapLocation());
        });



    }

    public void loadRoutes(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<RoutesCsvRecord> routesCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, RoutesCsvRecord.class);

        String sql = "INSERT INTO routes (route_id, user_user_id, name, description) VALUES(?, ?, ?, ?)";

        routesCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getUser(), userCsvRecord.getName(), userCsvRecord.getDescription());
        });


    }

    public void loadRoutesMapLocations(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<RouteMapLocationCsvRecord> routesMapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, RouteMapLocationCsvRecord.class);

        String sql = "INSERT INTO routes_map_locations (routes_map_location_id, map_location_id, route_id, sequence_nr) VALUES(?, ?, ?, ?)";

        routesMapLocationsCsv.forEach(userCsvRecord -> {
            jdbcTemplate.update(sql, userCsvRecord.getId(), userCsvRecord.getMapLocation(), userCsvRecord.getRoute(), userCsvRecord.getSequenceNr());
        });

    }


}
