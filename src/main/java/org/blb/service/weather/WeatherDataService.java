package org.blb.service.weather;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.blb.DTO.weather.WeatherDataResponseDto;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.models.weather.WeatherDataEntity;
import org.blb.repository.weather.WeatherRepository;
import org.blb.service.util.weatherMapping.WeatherConverter;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WeatherDataService implements WeatherDataServiceInterface{

    private final WeatherRepository repository;
    private final WeatherConverter weatherConverter;
    private final OutWeatherApi outWeatherApi;
    private final OutGeoLocationApi outGeoLocationApi;

    @Override
    @SneakyThrows
    public WeatherDataResponseDto getWeather(String ipAddress)  {
        WeatherLatLonDTO dto = getLatLonFromGeoLocation(ipAddress);

        Optional<WeatherDataEntity> optEntity = getFromDatabase(dto.getLat(), dto.getLon());

        if (optEntity.isPresent()){
            WeatherDataEntity existingEntity = optEntity.get();
            LocalDateTime createdTime = existingEntity.getTimeCreate();

            long duration = Duration.between(createdTime, LocalDateTime.now()).toMinutes();

            // If the latest data is less than 10 minutes old, return it
            if (duration < 10) {
                return weatherConverter.fromEntityToDto(existingEntity);
            }
        }
        // Fetch new data from API and update existing data
        WeatherDataResponseDto response = getFromApi(dto.getLat(), dto.getLon());
        WeatherDataEntity updatedEntity = weatherConverter.fromDtoToEntity(response);

        // Update the existing entity with new data
        if (optEntity.isPresent()){
            WeatherDataEntity existingEntity = optEntity.get();
            updateEntity(existingEntity, updatedEntity);
            repository.save(existingEntity);
        }else {
            repository.save(updatedEntity);
        }
        return response;
    }

    private Optional<WeatherDataEntity> getFromDatabase(String lat, String lon){
        return repository.findByLatitudeAndLongitude(lat,lon);
    }


    private WeatherDataResponseDto getFromApi(String lat, String lon) throws MalformedURLException, URISyntaxException {
        return outWeatherApi.receivedFromWeatherApi(lat,lon);
    }

    private WeatherLatLonDTO getLatLonFromGeoLocation(String ipAddress) throws MalformedURLException, URISyntaxException {
        return outGeoLocationApi.getLatLonFromGeoLocation(ipAddress);
    }

    private void updateEntity(WeatherDataEntity existingEntity, WeatherDataEntity newEntity) {
        existingEntity.setLatitude(newEntity.getLatitude());
        existingEntity.setLongitude(newEntity.getLongitude());
        existingEntity.setCityName(newEntity.getCityName());
        existingEntity.setTemperature(newEntity.getTemperature());
        existingEntity.setAppTemperature(newEntity.getAppTemperature());
        existingEntity.setIcon(newEntity.getIcon());
        existingEntity.setDescription(newEntity.getDescription());
        existingEntity.setHumidity(newEntity.getHumidity());
        existingEntity.setWindCdir(newEntity.getWindCdir());
        existingEntity.setWindCdirFull(newEntity.getWindCdirFull());
        existingEntity.setWindSpd(newEntity.getWindSpd());
        existingEntity.setTimeCreate(newEntity.getTimeCreate());
    }
}
