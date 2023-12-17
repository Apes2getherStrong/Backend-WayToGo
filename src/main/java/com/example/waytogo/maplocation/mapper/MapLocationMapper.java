package com.example.waytogo.maplocation.mapper;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.maplocation.model.entity.MapLocation;
import org.mapstruct.Mapper;

@Mapper
public interface MapLocationMapper {

    MapLocation mapLocationDtoToMapLocation(MapLocationDTO mapLocationDTO);

    MapLocationDTO mapLocationToMapLocationDto(MapLocation mapLocation);
}
