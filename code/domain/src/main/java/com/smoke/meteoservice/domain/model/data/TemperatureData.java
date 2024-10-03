package com.smoke.meteoservice.domain.model.data;

import com.smoke.meteoservice.domain.model.base.TemperatureBase;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "temperatureData")
public class TemperatureData extends TemperatureBase {

    @Id
    private String id;

    private LocalDateTime timestamp;

    public TemperatureData(double latitude, double longitude, double temperature) {
        super(latitude, longitude, temperature);
        this.timestamp = LocalDateTime.now();
    }
}
