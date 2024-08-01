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
            WeatherDataEntity weatherDataEntity = optEntity.get();
            LocalDateTime createdTime = weatherDataEntity.getTimeCreate();

            long duration = Duration.between(LocalDateTime.now(), createdTime).toMinutes();

            if (duration < 10) {
                return weatherConverter.fromEntityToDto(weatherDataEntity);
            }
        }

    WeatherDataResponseDto response = getFromApi(dto.getLat(), dto.getLon());
    repository.save(weatherConverter.fromDtoToEntity(response));
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
}
