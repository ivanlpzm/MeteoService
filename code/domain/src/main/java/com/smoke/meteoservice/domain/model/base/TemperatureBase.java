package com.smoke.meteoservice.domain.model.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class TemperatureBase {
    private double latitude;
    private double longitude;
    private double temperature;

    protected TemperatureBase(double latitude, double longitude, double temperature) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
    }
}
