
package com.smoke.meteoservice.domain.port.in;

import com.smoke.meteoservice.domain.model.response.TemperatureResponse;

public interface TemperatureUseCase {
    TemperatureResponse getTemperature(double latitude, double longitude);

    void deleteTemperature(double latitude, double longitude);

    /**
     * Fetches the latest temperature from the external API and updates the stored
     * record if present. If no record exists, it will be created.
     *
     * @param latitude  the latitude of the location
     * @param longitude the longitude of the location
     * @return the updated temperature response
     */
    TemperatureResponse updateTemperature(double latitude, double longitude);

}
