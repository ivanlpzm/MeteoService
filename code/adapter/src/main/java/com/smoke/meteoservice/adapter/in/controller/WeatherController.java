package com.smoke.meteoservice.adapter.in.controller;

import com.smoke.meteoservice.adapter.in.api.WeatherApi;
import com.smoke.meteoservice.adapter.in.response.TemperatureResponse;
import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.domain.port.in.WeatherUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WeatherController implements WeatherApi {

    @Autowired
    private WeatherUseCase weatherUseCase;

    @Override
    public TemperatureResponse getTemperature(@RequestParam double latitude, @RequestParam double longitude) {
        TemperatureData data = weatherUseCase.getTemperature(latitude, longitude);
        return new TemperatureResponse(data.getLatitude(), data.getLongitude(), data.getTemperature());
    }

    @Override
    public ResponseEntity<Void> deleteTemperature(@RequestParam double latitude, @RequestParam double longitude) {
        weatherUseCase.deleteTemperature(latitude, longitude);
        return ResponseEntity.noContent().build();
    }
}
