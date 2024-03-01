package com.example.waytogo.initialize.csvLoading.mapper;

import com.example.waytogo.audio.model.csvModel.AudioCsvRecord;
import com.example.waytogo.audio.model.entity.Audio;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import com.example.waytogo.route.model.csvModel.RoutesCsvRecord;
import com.example.waytogo.route.model.entity.Route;
import com.example.waytogo.routes_maplocation.csvModel.RouteMapLocationCsvRecord;
import com.example.waytogo.routes_maplocation.entity.RouteMapLocation;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import com.example.waytogo.user.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface RecordsCsvMapper {
    User userCsvRecordToUser(UserCsvRecord userCsvRecord);
    Audio audioCsvRecordToAudio(AudioCsvRecord audioCsvRecord);
    Route routeCsvRecordToRoute(RoutesCsvRecord routesCsvRecord);
    RouteMapLocation routeMapLocationCsvRecordToRoute(RouteMapLocationCsvRecord routeMapLocationCsvRecord);

    @Mapping(target = "id", source = "value")
    User mapUser(UUID value);

    @Mapping(target = "id", source = "value")
    MapLocation mapLocation(UUID value);
    @Mapping(target = "id", source = "value")
    Route mapRoute(UUID value);
}
