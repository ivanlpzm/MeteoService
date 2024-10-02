package com.smoke.meteoservice.application.service;

import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.domain.port.in.WeatherUseCase;
import com.smoke.meteoservice.domain.port.out.WeatherApi;
import com.smoke.meteoservice.domain.port.out.WeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherService implements WeatherUseCase {
    private final WeatherRepository weatherRepository;
    private final WeatherApi weatherApi;

    public WeatherService(WeatherRepository weatherRepository, WeatherApi weatherApi) {
        this.weatherRepository = weatherRepository;
        this.weatherApi = weatherApi;
    }

    @Override
    public TemperatureData getTemperature(double latitude, double longitude) {
        Optional<TemperatureData> cachedData = weatherRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (cachedData.isPresent() && cachedData.get().getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1))) {
            return cachedData.get();
        }

        double temperature = weatherApi.fetchTemperature(latitude, longitude);
        TemperatureData data = new TemperatureData();
        data.setLatitude(latitude);
        data.setLongitude(longitude);
        data.setTemperature(temperature);
        data.setTimestamp(LocalDateTime.now());

        return weatherRepository.save(data);
    }

    @Override
    public void deleteTemperature(double latitude, double longitude) {
        weatherRepository.findByLatitudeAndLongitude(latitude, longitude)
                .ifPresent(weatherRepository::delete);
    }

}
