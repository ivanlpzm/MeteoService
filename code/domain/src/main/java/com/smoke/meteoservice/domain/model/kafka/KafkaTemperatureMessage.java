package com.smoke.meteoservice.domain.model.kafka;


import com.smoke.meteoservice.domain.model.base.TemperatureBase;

public class KafkaTemperatureMessage extends TemperatureBase {

    public KafkaTemperatureMessage(double latitude, double longitude, double temperature) {
        super(latitude, longitude, temperature);
    }
}

