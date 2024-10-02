
package com.smoke.meteoservice.domain.port.in;

import com.smoke.meteoservice.domain.model.TemperatureData;

public interface WeatherUseCase {
    TemperatureData getTemperature(double latitude, double longitude);

    void deleteTemperature(double latitude, double longitude);
}
