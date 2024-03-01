package com.example.waytogo.route.model.csvModel;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutesCsvRecord {

    @CsvBindByName
    UUID id;

    @CsvBindByName(column = "user_id")
    UUID user;

    @CsvBindByName
    String name;

    @CsvBindByName
    String description;


}