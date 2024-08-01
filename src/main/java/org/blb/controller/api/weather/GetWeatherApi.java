package org.blb.controller.api.weather;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.weather.WeatherDataResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/weather")
//@CrossOrigin(origins = "http://localhost:5173")
public interface GetWeatherApi {
    @Operation(summary = "Get weather data by IP address", description = "Returns weather data for a given IP address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data",
                    content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WeatherDataResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid IP address provided",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Invalid IP address\" }"))),
            @ApiResponse(responseCode = "404", description = "Weather data not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Weather data not found for given IP address\" }"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: Various server errors can occur during processing.",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "General Error", value = "{ \"message\": \"Internal server error occurred\" }"),
                                    @ExampleObject(name = "JSON Processing Error", value = "{ \"message\": \"Error processing JSON response: error details here\" }")
                            }))
    })
    @GetMapping
    WeatherDataResponseDto getWeather(@RequestParam(required = false) String ipAddress);
}
