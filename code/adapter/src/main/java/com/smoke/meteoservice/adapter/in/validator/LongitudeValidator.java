package com.smoke.meteoservice.adapter.in.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongitudeValidator implements ConstraintValidator<ValidLongitude, Double> {

    @Override
    public boolean isValid(Double longitude, ConstraintValidatorContext context) {
        if (longitude == null) {
            return false;
        }

        return longitude >= -180 && longitude <= 180;
    }
}
