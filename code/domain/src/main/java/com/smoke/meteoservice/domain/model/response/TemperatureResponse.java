package com.smoke.meteoservice.domain.model.response;

import com.smoke.meteoservice.domain.model.base.TemperatureBase;

public class TemperatureResponse extends TemperatureBase {

    public TemperatureResponse(double latitude, double longitude, double temperature) {
        super(latitude, longitude, temperature);
    }
    
}
