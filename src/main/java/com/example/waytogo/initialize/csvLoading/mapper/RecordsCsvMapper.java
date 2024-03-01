package com.example.waytogo.initialize.csvLoading.mapper;

import com.example.waytogo.user.model.csvModel.UserCsvRecord;
import com.example.waytogo.user.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface RecordsCsvMapper {
    User userCsvRecordToUser(UserCsvRecord userCsvRecord);
}
