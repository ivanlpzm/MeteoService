package com.smoke.meteoservice.infrastructure.api;

import com.smoke.meteoservice.infrastructure.exception.OpenMeteoApiException;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenMeteoRestClientImplTest {

    private static final String API_URL = "http://api.open-meteo.com/v1/forecast";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json");

    private OkHttpClient mockClient;

    @InjectMocks
    private OpenMeteoRestClientImpl openMeteoApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return temperature when API call is successful")
    void fetchTemperature_shouldReturnTemperatureWhenApiCallIsSuccessful() throws IOException {
        mockClient = mockHttpClient(200, "{\"current_weather\":{\"temperature\": 25.0}}");
        openMeteoApi = new OpenMeteoRestClientImpl(mockClient, API_URL);

        double result = openMeteoApi.fetchTemperature(40.0, -3.0);

        assertEquals(25.0, result);
    }

    @Test
    @DisplayName("Should throw RuntimeException when API call fails")
    void fetchTemperature_shouldThrowRuntimeExceptionWhenApiCallFails() throws IOException {
        mockClient = mockHttpClient(500, "");
        openMeteoApi = new OpenMeteoRestClientImpl(mockClient, API_URL);

        assertThrows(RuntimeException.class, () -> openMeteoApi.fetchTemperature(40.0, -3.0));
    }

    @Test
    @DisplayName("Should throw IOException when response body is empty")
    void fetchTemperature_shouldThrowIOExceptionWhenResponseBodyIsEmpty() throws RuntimeException, IOException {
        mockClient = mockHttpClient(200, "");
        openMeteoApi = new OpenMeteoRestClientImpl(mockClient, API_URL);

        OpenMeteoApiException exception = assertThrows(OpenMeteoApiException.class, () -> openMeteoApi.fetchTemperature(40.0, -3.0));
        assertEquals("API call failed", exception.getMessage());
    }

    @Test
    @DisplayName("Should handle JSON array in response")
    void fetchTemperature_shouldHandleJsonArrayInResponse() throws IOException {
        mockClient = mockHttpClient(200, "[{\"current_weather\":{\"temperature\": 18.0}}]");
        openMeteoApi = new OpenMeteoRestClientImpl(mockClient, API_URL);

        double result = openMeteoApi.fetchTemperature(40.0, -3.0);

        assertEquals(18.0, result);
    }

    private static OkHttpClient mockHttpClient(int statusCode, String responseBody) throws IOException {
        OkHttpClient okHttpClient = mock(OkHttpClient.class);
        Call remoteCall = mock(Call.class);

        Response response = new Response.Builder()
                .request(new Request.Builder().url(API_URL).build())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .message("")
                .body(ResponseBody.create(responseBody, JSON_MEDIA_TYPE))
                .build();

        when(remoteCall.execute()).thenReturn(response);
        when(okHttpClient.newCall(any(Request.class))).thenReturn(remoteCall);

        return okHttpClient;
    }
}
