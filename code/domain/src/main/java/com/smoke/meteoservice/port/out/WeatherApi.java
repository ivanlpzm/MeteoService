
package com.smoke.meteoservice.port.out;

public interface WeatherApi {
    double fetchTemperature(double latitude, double longitude);
}
