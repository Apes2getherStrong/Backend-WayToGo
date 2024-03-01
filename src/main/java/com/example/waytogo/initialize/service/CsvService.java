package com.example.waytogo.initialize.service;


import java.io.File;
import java.util.List;

public interface CsvService<T> {
    List<T> convertCsvFileToCsvModel(File csvFile, Class<T> clazz);
}
