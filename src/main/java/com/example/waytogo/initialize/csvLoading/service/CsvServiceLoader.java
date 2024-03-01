package com.example.waytogo.initialize.csvLoading.service;

import com.example.waytogo.audio.repository.AudioRepository;
import com.example.waytogo.initialize.csvLoading.mapper.RecordsCsvMapper;
import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.maplocation.model.csvModel.MapLocationCsvRecord;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.maplocation.repository.MapLocationRepository;
import com.example.waytogo.route.repository.RouteRepository;
import com.example.waytogo.routes_maplocation.repository.RouteMapLocationRepository;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import com.example.waytogo.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
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
    private final RecordsCsvMapper recordsCsvMapper;

    public void loadUsers(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<UserCsvRecord> usersCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, UserCsvRecord.class);

        usersCsv.forEach(userCsvRecord -> {
            userRepository.save(recordsCsvMapper.userCsvRecordToUser(userCsvRecord));
        });
    }

    public void loadMapLocations(String filePath) throws FileNotFoundException {
        File file = ResourceUtils.getFile(filePath);

        List<MapLocationCsvRecord> mapLocationsCsv = CsvConverterGeneric.convertCsvFileToCsvModel(file, MapLocationCsvRecord.class);

         /**
         Jak bedzie dzialac to zamienic to na mappera tak zeby tworzyl pointy
         **/

        mapLocationsCsv.forEach(mapLocationCsvRecord -> {
            mapLocationRepository.save(
                    MapLocation.builder()
                            .id(mapLocationCsvRecord.getId())
                            .name(mapLocationCsvRecord.getName())
                            .description("brakuje opisu w csv")
                            .coordinates(geometryFactory.createPoint(new Coordinate(mapLocationCsvRecord.getCoord_x(), mapLocationCsvRecord.getCoord_y())))
                            .build()
            );
        });
    }

}
