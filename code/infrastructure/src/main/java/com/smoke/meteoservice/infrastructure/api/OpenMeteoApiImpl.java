package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.domain.port.out.api.OpenMeteoApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONObject;
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
        this.client = createHttpClient();
    }

    private OkHttpClient createHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::info);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Override
    public double fetchTemperature(double latitude, double longitude) {
        String url = buildUrl(latitude, longitude);
        try (Response response = executeRequest(url)) {
            validateResponse(response);
            String responseBody = getResponseBody(response);
            return extractTemperature(responseBody);
        } catch (IOException e) {
            log.error("Error while fetching temperature from OpenMeteo API", e);
            throw new RuntimeException("API call failed", e);
        }
    }

    private String buildUrl(double latitude, double longitude) {
        return String.format("%s?latitude=%f&longitude=%f&current_weather=true", apiUrl, latitude, longitude);
    }

    private Response executeRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    private void validateResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code: " + response);
        }
    }

    private String getResponseBody(Response response) throws IOException {
        if (response.body() == null) {
            throw new IOException("Response body is null");
        }
        return response.body().string();
    }

    private double extractTemperature(String responseBody) {
        JSONObject jsonObject = parseResponse(responseBody);
        JSONObject currentTemperature = jsonObject.getJSONObject(CURRENT_WEATHER);
        return currentTemperature.getDouble(TEMPERATURE);
    }

    private JSONObject parseResponse(String responseBody) {
        if (responseBody.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(responseBody);
            return jsonArray.getJSONObject(0);
        } else {
            return new JSONObject(responseBody);
        }
    }
}
