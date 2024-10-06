
package com.smoke.meteoservice.domain.port.out.api;

public interface OpenMeteoRestClient {
    double fetchTemperature(double latitude, double longitude);
}
