
package com.smoke.meteoservice.domain.port.out;

public interface MeteoApi {
    double fetchTemperature(double latitude, double longitude);
}
