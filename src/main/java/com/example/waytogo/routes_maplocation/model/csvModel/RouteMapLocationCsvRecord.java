package com.example.waytogo.routes_maplocation.model.csvModel;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteMapLocationCsvRecord {

    @CsvBindByName
    UUID id;

    @CsvBindByName(column = "map_location_id")
    UUID mapLocation;

    @CsvBindByName(column = "route_id")
    UUID route;

    @CsvBindByName(column = "sequence_nr")
    Integer sequenceNr;

}
