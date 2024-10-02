package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.domain.port.out.WeatherApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMeteoApiAdapter implements WeatherApi {

    public static final String CURRENT_WEATHER = "current_weather";
    public static final String TEMPERATURE = "temperature";

    @Value("${openmeteo.api.url}")
    private String apiUrl;

    @Override
    public double fetchTemperature(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?latitude=%f&longitude=%f&current_weather=true", apiUrl, latitude, longitude);
        String response = restTemplate.getForObject(url, String.class);
        JSONObject json = new JSONObject(response);
        return json.getJSONObject(CURRENT_WEATHER).getDouble(TEMPERATURE);
    }
}
