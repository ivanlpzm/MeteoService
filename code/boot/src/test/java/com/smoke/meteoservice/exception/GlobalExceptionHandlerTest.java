package com.smoke.meteoservice.exception;

import com.smoke.meteoservice.domain.model.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return 404 NOT_FOUND when NoHandlerFoundException is thrown")
    void handleNotFoundException_shouldReturnNotFound() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/invalid-url", null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The resource you are looking for was not found.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    @DisplayName("Should return 500 INTERNAL_SERVER_ERROR for generic exceptions")
    void handleInternalServerError_shouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInternalServerError(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("Unexpected error", response.getBody().getErrors().get("error"));
    }

    @Test
    @DisplayName("Should return 400 BAD_REQUEST for ConstraintViolationException")
    void handleConstraintViolationExceptions_shouldReturnBadRequest() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);

        when(mockPath.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("Invalid value");

        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Set.of(violation));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed for constraint violations.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("Invalid value", response.getBody().getErrors().get("fieldName"));
    }
}
