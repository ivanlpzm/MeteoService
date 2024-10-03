package com.smoke.meteoservice.adapter.in.controller;

import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.in.WeatherUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    private static final double LATITUDE = 40.0;
    private static final double LONGITUDE = -3.0;
    private static final double TEMPERATURE = 15.0;

    @Mock
    private WeatherUseCase weatherUseCase;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return temperature data successfully")
    void getTemperature_shouldReturnTemperatureSuccessfully() {
        TemperatureResponse mockResponse = new TemperatureResponse(LATITUDE, LONGITUDE, TEMPERATURE);
        when(weatherUseCase.getTemperature(anyDouble(), anyDouble())).thenReturn(mockResponse);

        ResponseEntity<TemperatureResponse> response = weatherController.getTemperature(LATITUDE, LONGITUDE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(weatherUseCase, times(1)).getTemperature(LATITUDE, LONGITUDE);
    }

    @Test
    @DisplayName("Should delete temperature data and return no content")
    void deleteTemperature_shouldReturnNoContent() {
        doNothing().when(weatherUseCase).deleteTemperature(anyDouble(), anyDouble());

        ResponseEntity<Void> response = weatherController.deleteTemperature(LATITUDE, LONGITUDE);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(weatherUseCase, times(1)).deleteTemperature(LATITUDE, LONGITUDE);
    }
}
