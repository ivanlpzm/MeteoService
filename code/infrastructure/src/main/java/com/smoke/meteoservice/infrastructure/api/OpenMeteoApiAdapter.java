package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.domain.port.out.MeteoApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenMeteoApiAdapter implements MeteoApi {

    public static final String CURRENT_WEATHER = "current_weather";
    public static final String TEMPERATURE = "temperature";

    @Value("${openmeteo.api.url}")
    private String apiUrl;

    @Override
    public double fetchTemperature(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?latitude=%f&longitude=%f&current_weather=true", apiUrl, latitude, longitude);
        String response = restTemplate.getForObject(url, String.class);
        
        JSONArray jsonArray = new JSONArray(response);
        
        if (!jsonArray.isEmpty()) {
            JSONObject weatherObject = jsonArray.getJSONObject(0);
            JSONObject currentWeather = weatherObject.getJSONObject(CURRENT_WEATHER);
            return currentWeather.getDouble(TEMPERATURE);
        } else {
            throw new RuntimeException("No weather data available");
        }
    }
}
