package com.example.waytogo.audio.model.csvModel;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioCsvRecord {

    @CsvBindByName
    UUID id;

    @CsvBindByName
    String name;

    @CsvBindByName
    String description;

    @CsvBindByName(column = "image_filename")
    String imageFilename;

    @CsvBindByName(column = "user_id")
    UUID user;

    @CsvBindByName(column = "map_location_id")
    UUID mapLocation;
}
