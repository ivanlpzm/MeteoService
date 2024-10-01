
package com.smoke.meteoservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "temperatureData")
public class TemperatureData {
    @Id
    private String id;

    private double latitude;

    private double longitude;

    private double temperature;
    
    private LocalDateTime timestamp;
}
