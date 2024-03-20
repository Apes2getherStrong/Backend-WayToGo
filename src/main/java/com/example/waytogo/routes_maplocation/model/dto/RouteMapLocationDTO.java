package com.example.waytogo.routes_maplocation.model.dto;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.route.model.dto.RouteDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteMapLocationDTO {
    private UUID id;

    private MapLocationDTO mapLocation;

    private RouteDTO route;

    @NotNull
    @Min(0)
    @Max(1000)
    private Integer sequenceNr;

}
