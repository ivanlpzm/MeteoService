package com.smoke.meteoservice.adapter.in.api;

import com.smoke.meteoservice.adapter.in.validator.ValidLatitude;
import com.smoke.meteoservice.adapter.in.validator.ValidLongitude;
import com.smoke.meteoservice.domain.model.response.TemperatureResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RequestMapping("/temperature")
@Tag(name = "Temperature Controller", description = "API for temperature data")
public interface TemperatureApi {

    @Operation(summary = "Get temperature by latitude and longitude", description = "Fetches the current temperature data for the given coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved temperature data",
                    content = {@Content(mediaType = "application/jinson", schema = @Schema(implementation = TemperatureResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Temperature data not found for the given coordinates")
    })
    @GetMapping
    ResponseEntity<TemperatureResponse> getTemperature(@ValidLatitude @RequestParam double latitude, @ValidLongitude @RequestParam double longitude);

    @Operation(summary = "Delete temperature data", description = "Deletes the temperature data for the given coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted temperature data"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Temperature data not found for the given coordinates")
    })
    @DeleteMapping
    ResponseEntity<Void> deleteTemperature(@ValidLatitude @RequestParam double latitude, @ValidLongitude @RequestParam double longitude);
}
