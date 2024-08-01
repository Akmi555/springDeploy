package org.blb.DTO.mainPageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="Main page weather")
public class MpWeatherDTO {
    @Schema(description = "Name of the city", example = "Berlin")
    private String city;
    @Schema(description = "Current temperature", example = "25")
    private String temp;
    @Schema(description = "Max temperature of the day", example = "27")
    private String max;
    @Schema(description = "Min temperature of the day", example = "20")
    private String min;
    @Schema(description = "Description of the weather", example = "Überwiegend bewölkt")
    private String description;
    @Schema(description = "Information about wind", example = "6.71")
    private String wind;
    @Schema(description = "Information about humidity", example = "63%")
    private String humidity;
    @Schema(description = "Icon of the weather", example = "10d")
    private String ico;
}
