package com.example.waytogo.maplocation.model.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.locationtech.jts.geom.Point;

public class CoordinatesValidator implements ConstraintValidator<ValidCoordinates, Point> {

    @Override
    public void initialize(ValidCoordinates constraintAnnotation) {
    }

    @Override
    public boolean isValid(Point value, ConstraintValidatorContext context) {
        if (value != null) {
            double latitude = value.getY();  // Assuming latitude is the second coordinate
            double longitude = value.getX(); // Assuming longitude is the first coordinate

            // Check if latitude is within the valid range (-90 to 90 degrees)
            boolean isValidLatitude = latitude >= -90.0 && latitude <= 90.0;

            // Check if longitude is within the valid range (-180 to 180 degrees)
            boolean isValidLongitude = longitude >= -180.0 && longitude <= 180.0;

            // Both latitude and longitude must be valid
            return isValidLatitude && isValidLongitude;
        }
        return true; // If the value is null, consider it valid (or you can change this logic based on your requirements)
    }
}

