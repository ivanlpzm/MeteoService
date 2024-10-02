package com.smoke.meteoservice.adapter.in.api;

import com.smoke.meteoservice.adapter.in.response.TemperatureResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/temperature")
@Tag(name = "Weather Controller", description = "API for weather data")
public interface WeatherApi {

    @Operation(summary = "Get temperature by latitude and longitude", description = "Fetches the current temperature data for the given coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved temperature data",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TemperatureResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Temperature data not found for the given coordinates")
    })
    @GetMapping
    TemperatureResponse getTemperature(@RequestParam double latitude, @RequestParam double longitude);

    @Operation(summary = "Delete temperature data", description = "Deletes the temperature data for the given coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted temperature data"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Temperature data not found for the given coordinates")
    })
    @DeleteMapping
    ResponseEntity<Void> deleteTemperature(@RequestParam double latitude, @RequestParam double longitude);
}
