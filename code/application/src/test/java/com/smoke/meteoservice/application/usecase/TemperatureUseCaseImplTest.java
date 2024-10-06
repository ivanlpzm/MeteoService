package com.smoke.meteoservice.application.usecase;

import com.smoke.meteoservice.domain.model.data.TemperatureData;
import com.smoke.meteoservice.domain.model.kafka.KafkaTemperatureMessage;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import com.smoke.meteoservice.domain.port.out.api.OpenMeteoRestClient;
import com.smoke.meteoservice.domain.port.out.kafka.KafkaProducerService;
import com.smoke.meteoservice.domain.port.out.repository.MongoWeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class TemperatureUseCaseImplTest {

    private static final double LATITUDE = 40.0;
    private static final double LONGITUDE = -3.0;
    private static final double TEMPERATURE = 15.0;

    @Mock
    private MongoWeatherRepository mongoWeatherRepository;

    @Mock
    private OpenMeteoRestClient openMeteoRestClient;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TemperatureUseCaseImpl weatherUseCase;

    private TemperatureData testData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testData = new TemperatureData(LATITUDE, LONGITUDE, TEMPERATURE);
    }

    @Test
    @DisplayName("Should return temperature from cache if found")
    void getTemperature_shouldReturnTemperatureFromCache() {
        when(mongoWeatherRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE))
                .thenReturn(Optional.of(testData));

        TemperatureResponse result = weatherUseCase.getTemperature(LATITUDE, LONGITUDE);

        assertEquals(LATITUDE, result.getLatitude());
        assertEquals(LONGITUDE, result.getLongitude());
        assertEquals(TEMPERATURE, result.getTemperature());
        verify(mongoWeatherRepository, times(1)).findByLatitudeAndLongitude(LATITUDE, LONGITUDE);
        verify(kafkaProducerService, times(1)).sendMessage(any(KafkaTemperatureMessage.class));
        verify(openMeteoRestClient, never()).fetchTemperature(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Should fetch and save temperature if not found in cache")
    void getTemperature_shouldFetchAndSaveTemperatureIfNotFound() {
        when(mongoWeatherRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE))
                .thenReturn(Optional.empty());
        when(openMeteoRestClient.fetchTemperature(LATITUDE, LONGITUDE)).thenReturn(TEMPERATURE);
        when(mongoWeatherRepository.save(any(TemperatureData.class))).thenReturn(testData);

        TemperatureResponse result = weatherUseCase.getTemperature(LATITUDE, LONGITUDE);

        assertEquals(LATITUDE, result.getLatitude());
        assertEquals(LONGITUDE, result.getLongitude());
        assertEquals(TEMPERATURE, result.getTemperature());
        verify(mongoWeatherRepository, times(1)).findByLatitudeAndLongitude(LATITUDE, LONGITUDE);
        verify(openMeteoRestClient, times(1)).fetchTemperature(LATITUDE, LONGITUDE);
        verify(mongoWeatherRepository, times(1)).save(any(TemperatureData.class));
        verify(kafkaProducerService, times(1)).sendMessage(any(KafkaTemperatureMessage.class));
    }

    @Test
    @DisplayName("Should delete temperature data if found")
    void deleteTemperature_shouldDeleteTemperatureIfFound() {
        when(mongoWeatherRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE))
                .thenReturn(Optional.of(testData));

        weatherUseCase.deleteTemperature(LATITUDE, LONGITUDE);

        verify(mongoWeatherRepository, times(1)).findByLatitudeAndLongitude(LATITUDE, LONGITUDE);
        verify(mongoWeatherRepository, times(1)).delete(testData);
    }

    @Test
    @DisplayName("Should do nothing if temperature data not found during deletion")
    void deleteTemperature_shouldDoNothingIfTemperatureNotFound() {
        when(mongoWeatherRepository.findByLatitudeAndLongitude(LATITUDE, LONGITUDE))
                .thenReturn(Optional.empty());

        weatherUseCase.deleteTemperature(LATITUDE, LONGITUDE);

        verify(mongoWeatherRepository, times(1)).findByLatitudeAndLongitude(LATITUDE, LONGITUDE);
        verify(mongoWeatherRepository, never()).delete(any(TemperatureData.class));
    }
}
