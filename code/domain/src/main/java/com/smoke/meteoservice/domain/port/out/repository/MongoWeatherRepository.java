
package com.smoke.meteoservice.domain.port.out.repository;

import com.smoke.meteoservice.domain.model.data.TemperatureData;

import java.util.Optional;

public interface MongoWeatherRepository {
    Optional<TemperatureData> findByLatitudeAndLongitude(double latitude, double longitude);

    TemperatureData save(TemperatureData data);

    void delete(TemperatureData data);
}
