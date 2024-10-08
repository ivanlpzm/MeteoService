package com.smoke.meteoservice.application.usecase;

import com.smoke.meteoservice.domain.model.data.TemperatureData;
import com.smoke.meteoservice.domain.model.kafka.KafkaTemperatureMessage;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.in.TemperatureUseCase;
import com.smoke.meteoservice.domain.port.out.api.OpenMeteoRestClient;
import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import com.smoke.meteoservice.domain.port.out.repository.MongoWeatherRepository;
import org.springframework.stereotype.Service;

@Service
public class TemperatureUseCaseImpl implements TemperatureUseCase {

    private final MongoWeatherRepository mongoWeatherRepository;
    private final OpenMeteoRestClient openMeteoRestClient;
    private final KafkaProducerService kafkaProducerService;

    public TemperatureUseCaseImpl(MongoWeatherRepository mongoWeatherRepository, OpenMeteoRestClient openMeteoRestClient, KafkaProducerService kafkaProducerService) {
        this.mongoWeatherRepository = mongoWeatherRepository;
        this.openMeteoRestClient = openMeteoRestClient;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public TemperatureResponse getTemperature(double latitude, double longitude) {
        TemperatureData data = mongoWeatherRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> fetchAndSaveTemperature(latitude, longitude));

        sendWeatherMessageToKafka(data);
        return new TemperatureResponse(data.getLatitude(), data.getLongitude(), data.getTemperature());
    }

    @Override
    public void deleteTemperature(double latitude, double longitude) {
        mongoWeatherRepository.findByLatitudeAndLongitude(latitude, longitude)
                .ifPresent(mongoWeatherRepository::delete);
    }

    private TemperatureData fetchAndSaveTemperature(double latitude, double longitude) {
        double temperature = openMeteoRestClient.fetchTemperature(latitude, longitude);
        TemperatureData data = new TemperatureData(latitude, longitude, temperature);
        return mongoWeatherRepository.save(data);
    }

    private void sendWeatherMessageToKafka(TemperatureData data) {
        KafkaTemperatureMessage kafkaMessage = new KafkaTemperatureMessage(data.getLatitude(), data.getLongitude(), data.getTemperature());
        kafkaProducerService.sendMessage(kafkaMessage);
    }
}
