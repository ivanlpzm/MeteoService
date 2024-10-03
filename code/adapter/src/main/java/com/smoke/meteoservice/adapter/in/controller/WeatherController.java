package com.smoke.meteoservice.adapter.in.controller;

import com.smoke.meteoservice.adapter.in.api.WeatherApi;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.in.TemperatureUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController implements WeatherApi {

    @Autowired
    private TemperatureUseCase temperatureUseCase;

    @Override
    public ResponseEntity<TemperatureResponse> getTemperature(double latitude, double longitude) {
        TemperatureResponse temperatureResponse = temperatureUseCase.getTemperature(latitude, longitude);
        return new ResponseEntity<>(temperatureResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTemperature(double latitude, double longitude) {
        temperatureUseCase.deleteTemperature(latitude, longitude);
        return ResponseEntity.noContent().build();
    }
}
