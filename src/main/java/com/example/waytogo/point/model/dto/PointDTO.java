package com.example.waytogo.point.model.dto;

import com.example.waytogo.point.model.entity.Coordinates;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PointDTO {
    private UUID id;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;
    @NotNull
    @Valid
    private CoordinatesDTO coordinates;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
