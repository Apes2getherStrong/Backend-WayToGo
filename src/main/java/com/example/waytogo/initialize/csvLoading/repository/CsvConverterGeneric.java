package com.example.waytogo.initialize.csvLoading.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvConverterGeneric {
    public static <T> List<T> convertCsvFileToCsvModel(InputStream inputStream, Class<T> clazz) {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .build()
                .parse();
    }
}