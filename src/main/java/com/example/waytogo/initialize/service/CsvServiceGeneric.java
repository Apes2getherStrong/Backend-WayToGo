package com.example.waytogo.initialize.service;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class CsvServiceGeneric<T> implements CsvService<T> {

    @Override
    public List<T> convertCsvFileToCsvModel(File csvFile, Class<T> clazz) {
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
