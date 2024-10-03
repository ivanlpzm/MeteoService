package com.smoke.meteoservice.adapter.in.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LatitudeValidatorTest {

    private LatitudeValidator latitudeValidator;

    @BeforeEach
    void setUp() {
        latitudeValidator = new LatitudeValidator();
    }

    @Test
    @DisplayName("Should return true for valid latitude")
    void isValid_shouldReturnTrueForValidLatitude() {
        assertTrue(latitudeValidator.isValid(45.0, null));
        assertTrue(latitudeValidator.isValid(-90.0, null));
        assertTrue(latitudeValidator.isValid(90.0, null));
    }

    @Test
    @DisplayName("Should return false for invalid latitude")
    void isValid_shouldReturnFalseForInvalidLatitude() {
        assertFalse(latitudeValidator.isValid(null, null));
        assertFalse(latitudeValidator.isValid(-100.0, null));
        assertFalse(latitudeValidator.isValid(100.0, null));
    }
}
