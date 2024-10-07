package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.domain.port.out.api.OpenMeteoRestClient;
import com.smoke.meteoservice.infrastructure.exception.OpenMeteoApiException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OpenMeteoRestClientImpl implements OpenMeteoRestClient {

    public static final String CURRENT_WEATHER = "current_weather";
    public static final String TEMPERATURE = "temperature";

    @Value("${openmeteo.api.url}")
    private String apiUrl;

    private final OkHttpClient client;

    public OpenMeteoRestClientImpl() {
        this.client = createHttpClient();
    }

    protected OpenMeteoRestClientImpl(OkHttpClient client, String apiUrl) {
        this.client = client != null ? client : createHttpClient();
        this.apiUrl = apiUrl;
    }

    private OkHttpClient createHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::debug);
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
            throw new OpenMeteoApiException("API call failed", e);
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
            throw new IOException("Unexpected response code: " + response.code() + " with message: " + response.message());
        }
    }

    private String getResponseBody(Response response) throws IOException {
        if (response.body() == null) {
            throw new IOException("Response body is null");
        }
        String bodyContent = response.body().string();
        if (bodyContent.isEmpty()) {
            throw new IOException("Response body is empty");
        }
        return bodyContent;
    }

    private double extractTemperature(String responseBody) throws IOException {
        JSONObject jsonObject = parseResponse(responseBody);
        JSONObject currentTemperature;
        try {
            currentTemperature = jsonObject.getJSONObject(CURRENT_WEATHER);
        } catch (JSONException e) {
            throw new IOException("Failed to parse 'current_weather' JSON object", e);
        }
        return currentTemperature.getDouble(TEMPERATURE);
    }

    private JSONObject parseResponse(String responseBody) throws IOException {
        try {
            if (responseBody.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(responseBody);
                return jsonArray.getJSONObject(0);
            } else {
                return new JSONObject(responseBody);
            }
        } catch (JSONException e) {
            throw new IOException("Malformed JSON response", e);
        }
    }
}
