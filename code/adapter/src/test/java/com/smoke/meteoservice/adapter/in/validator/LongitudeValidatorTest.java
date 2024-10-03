package com.smoke.meteoservice.adapter.in.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongitudeValidatorTest {

    private LongitudeValidator longitudeValidator;

    @BeforeEach
    void setUp() {
        longitudeValidator = new LongitudeValidator();
    }

    @Test
    @DisplayName("Should return true for valid longitude")
    void isValid_shouldReturnTrueForValidLongitude() {
        assertTrue(longitudeValidator.isValid(0.0, null));
        assertTrue(longitudeValidator.isValid(-180.0, null));
        assertTrue(longitudeValidator.isValid(180.0, null));
    }

    @Test
    @DisplayName("Should return false for invalid longitude")
    void isValid_shouldReturnFalseForInvalidLongitude() {
        assertFalse(longitudeValidator.isValid(null, null));
        assertFalse(longitudeValidator.isValid(-200.0, null));
        assertFalse(longitudeValidator.isValid(200.0, null));
    }
}
