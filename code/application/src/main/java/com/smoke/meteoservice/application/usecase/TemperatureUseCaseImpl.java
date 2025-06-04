package com.smoke.meteoservice.application.usecase;

import com.smoke.meteoservice.domain.model.data.TemperatureData;
import com.smoke.meteoservice.domain.model.kafka.KafkaTemperatureMessage;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.in.TemperatureUseCase;
import com.smoke.meteoservice.domain.port.out.api.OpenMeteoRestClient;
import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import com.smoke.meteoservice.domain.port.out.repository.MongoWeatherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TemperatureUseCaseImpl implements TemperatureUseCase {

    private final MongoWeatherRepository mongoWeatherRepository;
    private final OpenMeteoRestClient openMeteoRestClient;
    private final KafkaProducerService kafkaProducerService;

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

    @Override
    public TemperatureResponse updateTemperature(double latitude, double longitude) {
        TemperatureData updated = mongoWeatherRepository.findByLatitudeAndLongitude(latitude, longitude)
                .map(existing -> refreshExistingTemperature(existing, latitude, longitude))
                .orElseGet(() -> fetchAndSaveTemperature(latitude, longitude));

        sendWeatherMessageToKafka(updated);
        return new TemperatureResponse(updated.getLatitude(), updated.getLongitude(), updated.getTemperature());
    }

    private TemperatureData refreshExistingTemperature(TemperatureData existing, double latitude, double longitude) {
        double newTemp = openMeteoRestClient.fetchTemperature(latitude, longitude);
        existing.setTemperature(newTemp);
        existing.setTimestamp(java.time.LocalDateTime.now());
        return mongoWeatherRepository.save(existing);
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
