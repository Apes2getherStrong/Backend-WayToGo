package com.example.waytogo.maplocation.model.entity.coordinateTools;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CoordinatesValidator.class)
public @interface ValidCoordinates {
    String message() default "Invalid coordinates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
