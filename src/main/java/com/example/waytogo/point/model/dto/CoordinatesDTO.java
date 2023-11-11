package com.example.waytogo.point.model.dto;

import com.example.waytogo.point.model.entity.Point;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CoordinatesDTO {
    private Double latitude;
    private Double longitude;
}
