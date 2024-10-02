
package com.smoke.meteoservice.domain.port.out.api;

public interface OpenMeteoApi {
    double fetchTemperature(double latitude, double longitude);
}
