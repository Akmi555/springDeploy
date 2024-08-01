package org.blb.controller.weatherController;


import lombok.AllArgsConstructor;
import org.blb.DTO.weather.WeatherDataResponseDto;
import org.blb.controller.api.weather.GetWeatherApi;
import org.blb.service.weather.WeatherDataServiceInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class GetWeatherController implements GetWeatherApi {
    private final WeatherDataServiceInterface service;

    @Override
    @GetMapping
    public WeatherDataResponseDto getWeather(@RequestParam(required = false) String ipAddress) {
        String defaultIpAddress = "85.214.132.117"; // from Strato AG for Berlin
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = defaultIpAddress;
        }
        return service.getWeather(ipAddress);
    }
}
