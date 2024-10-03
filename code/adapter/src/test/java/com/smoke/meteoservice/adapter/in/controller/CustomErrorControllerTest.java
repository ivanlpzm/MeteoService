package com.smoke.meteoservice.adapter.in.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomErrorControllerTest {

    private static final String ERROR_404_PAGE = "error/404";
    private static final String GENERIC_ERROR_PAGE = "error/error";

    private CustomErrorController customErrorController;

    @BeforeEach
    void setUp() {
        customErrorController = new CustomErrorController();
    }

    @Test
    @DisplayName("Should return 404 error page when status code is 404")
    void handleError_shouldReturn404PageForNotFoundError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(HTTP_NOT_FOUND);

        String result = customErrorController.handleError(request);

        assertEquals(ERROR_404_PAGE, result);
    }

    @Test
    @DisplayName("Should return generic error page for other errors")
    void handleError_shouldReturnGenericErrorPageForOtherErrors() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(500);

        String result = customErrorController.handleError(request);

        assertEquals(GENERIC_ERROR_PAGE, result);
    }

    @Test
    @DisplayName("Should return generic error page when status code is null")
    void handleError_shouldReturnGenericErrorPageForNullStatusCode() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(null);

        String result = customErrorController.handleError(request);

        assertEquals(GENERIC_ERROR_PAGE, result);
    }
}
