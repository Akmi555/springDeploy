package org.blb.DTO.weather;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WeatherDataRequestDto {
    @NotBlank @NotNull
    private String latitude;

    @NotBlank @NotNull
    private String longitude;
}
