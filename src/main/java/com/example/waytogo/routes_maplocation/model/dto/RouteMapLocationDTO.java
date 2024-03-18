package com.example.waytogo.routes_maplocation.model.dto;

import com.example.waytogo.maplocation.model.dto.MapLocationDTO;
import com.example.waytogo.route.model.dto.RouteDTO;
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

    private Integer sequenceNr;

}
