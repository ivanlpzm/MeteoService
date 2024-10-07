package com.smoke.meteoservice.adapter.in.controller;

import com.smoke.meteoservice.adapter.in.api.TemperatureApi;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.in.TemperatureUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TemperatureController implements TemperatureApi {

    @Autowired
    private TemperatureUseCase temperatureUseCase;

    @Override
    public ResponseEntity<TemperatureResponse> getTemperature(double latitude, double longitude) {
        log.info("Fetching temperature data for latitude {} and longitude {}", latitude, longitude);
        TemperatureResponse temperatureResponse = temperatureUseCase.getTemperature(latitude, longitude);
        return new ResponseEntity<>(temperatureResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteTemperature(double latitude, double longitude) {
        log.info("Deleting temperature data for latitude {} and longitude {}", latitude, longitude);
        temperatureUseCase.deleteTemperature(latitude, longitude);
        return ResponseEntity.noContent().build();
    }
}
