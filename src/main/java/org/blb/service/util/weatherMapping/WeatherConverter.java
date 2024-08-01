package org.blb.service.util.weatherMapping;

import org.blb.DTO.weather.WeatherDataResponseDto;
import org.blb.models.weather.WeatherDataEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WeatherConverter {

    public WeatherDataResponseDto fromEntityToDto(WeatherDataEntity entity){
        WeatherDataResponseDto response = new WeatherDataResponseDto();
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());
        response.setCity(entity.getCityName());
        response.setTemp(entity.getTemperature());
        response.setApp_temp(entity.getAppTemperature());
        response.setDescription(entity.getDescription());
        response.setWind(entity.getWindSpd());
        response.setWind_dir(entity.getWindCdir());
        response.setWind_dir_full(entity.getWindCdirFull());
        response.setHumidity(entity.getHumidity());
        response.setIco(entity.getIcon());

        return response;
    }

    public WeatherDataEntity fromDtoToEntity(WeatherDataResponseDto dto){
        WeatherDataEntity entity = new WeatherDataEntity();
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setCityName(dto.getCity());
        entity.setTemperature(dto.getTemp());
        entity.setAppTemperature(dto.getApp_temp());
        entity.setDescription(dto.getDescription());
        entity.setWindSpd(dto.getWind());
        entity.setWindCdir(dto.getWind_dir());
        entity.setWindCdirFull(dto.getWind_dir_full());
        entity.setHumidity(dto.getHumidity());
        entity.setIcon(dto.getIco());
        entity.setTimeCreate(LocalDateTime.now());

        return entity;
    }
}
