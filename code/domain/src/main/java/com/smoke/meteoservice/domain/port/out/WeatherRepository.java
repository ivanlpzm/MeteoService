
package com.smoke.meteoservice.domain.port.out;

import com.smoke.meteoservice.domain.model.TemperatureData;

import java.util.Optional;

public interface WeatherRepository {
    Optional<TemperatureData> findByLatitudeAndLongitude(double latitude, double longitude);

    TemperatureData save(TemperatureData data);

    void delete(TemperatureData data);
}
