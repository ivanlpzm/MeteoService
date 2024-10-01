
package com.smoke.meteoservice.port.out;

import com.smoke.meteoservice.model.TemperatureData;

import java.util.Optional;

public interface WeatherRepository {
    Optional<TemperatureData> findByLatitudeAndLongitude(double latitude, double longitude);
    TemperatureData save(TemperatureData data);
}
