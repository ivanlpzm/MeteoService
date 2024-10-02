package com.smoke.meteoservice.adapter.in.controller;


import com.smoke.meteoservice.adapter.in.response.TemperatureResponse;
import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.application.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public TemperatureResponse getTemperature(@RequestParam double latitude, @RequestParam double longitude) {
        TemperatureData data = weatherService.getTemperature(latitude, longitude);
        return new TemperatureResponse(data.getLatitude(), data.getLongitude(), data.getTemperature());
    }

    @DeleteMapping
    public void deleteTemperature(@RequestParam double latitude, @RequestParam double longitude) {
        weatherService.deleteTemperature(latitude, longitude);
    }
}

