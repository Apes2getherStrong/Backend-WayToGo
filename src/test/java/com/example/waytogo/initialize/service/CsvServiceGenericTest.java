package com.example.waytogo.initialize.service;

import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CsvServiceGenericTest {
    @Autowired
    private CsvServiceGeneric<UserCsvRecord> csvService;

    @Test
    public void testConvertCsvFileToCsvModel() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvData/users.csv");

        List<UserCsvRecord> csvModels = csvService.convertCsvFileToCsvModel(file, UserCsvRecord.class);

        assertFalse(csvModels.isEmpty(), "Lista wynikowa nie powinna być pusta");
//        for (UserCsvRecord usr: csvModels){
//            System.out.println(usr);
//        }
    }
}