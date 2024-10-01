
package com.smoke.meteoservice.port.in;

import com.smoke.meteoservice.model.TemperatureData;

public interface WeatherUseCase {
    TemperatureData getTemperature(double latitude, double longitude);
}
