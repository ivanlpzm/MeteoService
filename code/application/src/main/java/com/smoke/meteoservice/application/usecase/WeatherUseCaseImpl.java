package com.smoke.meteoservice.application.usecase;

import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.domain.port.in.WeatherUseCase;
import com.smoke.meteoservice.domain.port.out.api.OpenMeteoApi;
import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import com.smoke.meteoservice.domain.port.out.repository.MongoWeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherUseCaseImpl implements WeatherUseCase {
    private final MongoWeatherRepository mongoWeatherRepository;
    private final OpenMeteoApi openMeteoApi;
    private final KafkaProducerService kafkaProducerService;

    public WeatherUseCaseImpl(MongoWeatherRepository mongoWeatherRepository, OpenMeteoApi openMeteoApi, KafkaProducerService kafkaProducerService) {
        this.mongoWeatherRepository = mongoWeatherRepository;
        this.openMeteoApi = openMeteoApi;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public TemperatureData getTemperature(double latitude, double longitude) {
        Optional<TemperatureData> cachedData = mongoWeatherRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (cachedData.isPresent()) {
            return cachedData.get();
        }

        double temperature = openMeteoApi.fetchTemperature(latitude, longitude);
        TemperatureData data = new TemperatureData();
        data.setLatitude(latitude);
        data.setLongitude(longitude);
        data.setTemperature(temperature);
        data.setTimestamp(LocalDateTime.now());

        kafkaProducerService.sendMessage(data);
        return mongoWeatherRepository.save(data);
    }

    @Override
    public void deleteTemperature(double latitude, double longitude) {
        mongoWeatherRepository.findByLatitudeAndLongitude(latitude, longitude)
                .ifPresent(mongoWeatherRepository::delete);
    }

}
