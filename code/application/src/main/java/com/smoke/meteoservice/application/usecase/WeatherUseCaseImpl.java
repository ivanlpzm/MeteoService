package com.smoke.meteoservice.application.usecase;

import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.domain.port.in.WeatherUseCase;
import com.smoke.meteoservice.domain.port.out.MeteoApi;
import com.smoke.meteoservice.domain.port.out.WeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherUseCaseImpl implements WeatherUseCase {
    private final WeatherRepository weatherRepository;
    private final MeteoApi meteoApi;

    public WeatherUseCaseImpl(WeatherRepository weatherRepository, MeteoApi meteoApi) {
        this.weatherRepository = weatherRepository;
        this.meteoApi = meteoApi;
    }

    @Override
    public TemperatureData getTemperature(double latitude, double longitude) {
        Optional<TemperatureData> cachedData = weatherRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (cachedData.isPresent()) {
            return cachedData.get();
        }

        double temperature = meteoApi.fetchTemperature(latitude, longitude);
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
