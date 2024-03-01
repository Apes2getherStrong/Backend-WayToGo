package com.example.waytogo.maplocation.model.csvModel;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationCsvRecord {

    @CsvBindByName
    UUID id;

    @CsvBindByName
    String name;

    @CsvBindByName
    String description;

    @CsvBindByName
    Float coord_x;

    @CsvBindByName
    Float coord_y;

}
