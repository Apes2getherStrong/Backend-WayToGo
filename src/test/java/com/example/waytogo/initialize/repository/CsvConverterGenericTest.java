package com.example.waytogo.initialize.repository;

import com.example.waytogo.initialize.csvLoading.repository.CsvConverterGeneric;
import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CsvConverterGenericTest {

    @Test
    public void testConvertCsvFileToCsvModel() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvData/users.csv");

        List<UserCsvRecord> csvModels = CsvConverterGeneric.convertCsvFileToCsvModel(file, UserCsvRecord.class);

        assertFalse(csvModels.isEmpty(), "Lista wynikowa nie powinna być pusta");
//        for (UserCsvRecord usr: csvModels){
//            System.out.println(usr);
//        }
    }
}