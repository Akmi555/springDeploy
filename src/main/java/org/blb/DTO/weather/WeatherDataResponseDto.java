package org.blb.DTO.weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="Response: weather data")
public class WeatherDataResponseDto {
    @Schema(description = "Latitude coordinate of the location", example = "52.5200")
    private String latitude;
    @Schema(description = "Longitude coordinate of the location", example = "13.4050")
    private String longitude;
    @Schema(description = "Name of the city", example = "Berlin")
    private String city;
    @Schema(description = "Current temperature", example = "25")
    private String temp;
    @Schema(description = "Apparent/'Feels Like' temperature", example = "22")
    private String app_temp;
    @Schema(description = "Description of the weather", example = "Wenige Wolken")
    private String description;
    @Schema(description = "Information about wind speed (m/s)", example = "6.7")
    private String wind;
    @Schema(description = "Information about wind direction", example = "NW")
    private String wind_dir;
    @Schema(description = "Information about wind direction full", example = "Nordwestlich")
    private String wind_dir_full;
    @Schema(description = "Information about humidity (%)", example = "52")
    private String humidity;
    @Schema(description = "Icon of the weather", example = "c02n")
    private String ico;
}
