
package com.smoke.meteoservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureResponse {
    private double latitude;
    private double longitude;
    private double temperature;
}
