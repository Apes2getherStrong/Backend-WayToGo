package com.example.waytogo.point.model.dto;

import com.example.waytogo.point.model.entity.Coordinates;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PointDTO {
    private UUID uuid;
    private String name;
    private CoordinatesDTO coordinates;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
