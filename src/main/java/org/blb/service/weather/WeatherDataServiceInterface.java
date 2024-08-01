package org.blb.service.weather;


import org.blb.DTO.weather.WeatherDataResponseDto;

public interface WeatherDataServiceInterface {

    WeatherDataResponseDto getWeather(String ipAddress);

}
