package com.example.waytogo.maplocation.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MapLocationDTO {
    private UUID id;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Point coordinates;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

}
