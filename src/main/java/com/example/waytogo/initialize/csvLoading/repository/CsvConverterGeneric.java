package com.example.waytogo.initialize.csvLoading.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Component
public class CsvConverterGeneric {
    public static <T> List<T> convertCsvFileToCsvModel(File csvFile, Class<T> clazz) {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(csvFile))
                    .withType(clazz)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
