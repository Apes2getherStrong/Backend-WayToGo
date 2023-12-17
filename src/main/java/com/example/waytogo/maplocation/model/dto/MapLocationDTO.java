package com.example.waytogo.maplocation.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MapLocationDTO {
    private UUID id;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    private Point coordinates;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

}
