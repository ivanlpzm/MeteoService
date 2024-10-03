
package com.smoke.meteoservice.infrastructure.repository;


import com.smoke.meteoservice.domain.model.data.TemperatureData;
import com.smoke.meteoservice.domain.port.out.repository.MongoWeatherRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWeatherRepositoryImpl extends MongoRepository<TemperatureData, String>, MongoWeatherRepository {
    
    @Override
    Optional<TemperatureData> findByLatitudeAndLongitude(double latitude, double longitude);

    @Override
    TemperatureData save(TemperatureData data);

    @Override
    void delete(TemperatureData data);
}
