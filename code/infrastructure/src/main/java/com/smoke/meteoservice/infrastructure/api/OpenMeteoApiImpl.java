package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.domain.port.out.api.OpenMeteoApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OpenMeteoApiImpl implements OpenMeteoApi {
    
    public static final String CURRENT_WEATHER = "current_weather";
    public static final String TEMPERATURE = "temperature";

    @Value("${openmeteo.api.url}")
    private String apiUrl;

    private final OkHttpClient client;

    public OpenMeteoApiImpl() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::info);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        this.client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Override
    public double fetchTemperature(double latitude, double longitude) {
        String url = String.format("%s?latitude=%f&longitude=%f&current_weather=true", apiUrl, latitude, longitude);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }

            String responseBody = response.body().string();
            log.info("API Response: {}", responseBody);

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject currentTemperature = jsonObject.getJSONObject(CURRENT_WEATHER);
            return currentTemperature.getDouble(TEMPERATURE);
        } catch (IOException e) {
            log.error("Error while fetching temperature from OpenMeteo API", e);
            throw new RuntimeException("API call failed", e);
        }
    }
}
