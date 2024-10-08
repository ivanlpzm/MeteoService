
package com.smoke.meteoservice.domain.port.in;

import com.smoke.meteoservice.domain.model.response.TemperatureResponse;

public interface TemperatureUseCase {
    TemperatureResponse getTemperature(double latitude, double longitude);

    void deleteTemperature(double latitude, double longitude);
}
