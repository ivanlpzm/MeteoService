
package com.smoke.meteoservice.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "temperatureData")
public class TemperatureData {

    @Id
    private String id;

    private double latitude;

    private double longitude;

    private double temperature;
}
