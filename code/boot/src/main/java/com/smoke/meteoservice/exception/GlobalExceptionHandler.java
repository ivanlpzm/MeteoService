package com.smoke.meteoservice.exception;

import com.smoke.meteoservice.domain.model.response.ErrorResponse;
import com.smoke.meteoservice.infrastructure.exception.OpenMeteoApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";
    private static final String ERROR_MESSAGE_NOT_FOUND = "The resource you are looking for was not found.";
    private static final String ERROR_MESSAGE_INTERNAL_SERVER = "An unexpected error occurred. Please try again later.";
    private static final String ERROR_MESSAGE_VALIDATION_FAILED = "Validation failed for constraint violations.";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException ex) {
        return buildErrorResponse(ERROR_MESSAGE_NOT_FOUND, null, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OpenMeteoApiException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(OpenMeteoApiException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR, ex.getMessage());
        return buildErrorResponse(ERROR_MESSAGE_INTERNAL_SERVER, errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return buildErrorResponse(ERROR_MESSAGE_VALIDATION_FAILED, errors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorMessage, Map<String, String> errors, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                errorMessage,
                errors != null ? errors : new HashMap<>()
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
