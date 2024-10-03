package com.smoke.meteoservice.adapter.in.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatitudeValidator implements ConstraintValidator<ValidLatitude, Double> {

    @Override
    public boolean isValid(Double latitude, ConstraintValidatorContext context) {
        if (latitude == null) {
            return false;
        }

        return latitude >= -90 && latitude <= 90;
    }
}
