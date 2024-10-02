
package com.smoke.meteoservice.infrastructure.repository;


import com.smoke.meteoservice.domain.model.TemperatureData;
import com.smoke.meteoservice.domain.port.out.WeatherRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWeatherRepository extends MongoRepository<TemperatureData, String>, WeatherRepository {
    
    @Override
    Optional<TemperatureData> findByLatitudeAndLongitude(double latitude, double longitude);

    @Override
    TemperatureData save(TemperatureData data);

    @Override
    void delete(TemperatureData data);
}
