package com.example.waytogo.user.model.csvModel;


import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCsvRecord {

    @CsvBindByName
    UUID id;

    @CsvBindByName
    String username;

    @CsvBindByName
    String password;

    @CsvBindByName
    String login;

}
