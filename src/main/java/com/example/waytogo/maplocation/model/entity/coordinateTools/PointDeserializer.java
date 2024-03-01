package com.example.waytogo.maplocation.model.entity.coordinateTools;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class PointDeserializer extends JsonDeserializer<Point> {

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        double x = node.get("coordinates").get(0).asDouble();
        double y = node.get("coordinates").get(1).asDouble();

        GeometryFactory geometryFactory = new GeometryFactory();
        CoordinateSequence coordinateSequence = geometryFactory.getCoordinateSequenceFactory().create(1, 2);

        coordinateSequence.setOrdinate(0, 0, x);
        coordinateSequence.setOrdinate(0, 1, y);

        return new Point(coordinateSequence, geometryFactory);
    }
}
