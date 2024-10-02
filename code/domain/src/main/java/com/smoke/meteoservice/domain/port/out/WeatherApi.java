
package com.smoke.meteoservice.domain.port.out;

public interface WeatherApi {
    double fetchTemperature(double latitude, double longitude);
}
