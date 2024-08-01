package org.blb.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.blb.DTO.weather.WeatherDataResponseDto;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.DTO.weather.weatherJsonDataModel.WeatherJSON;
import org.blb.exeption.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
@AllArgsConstructor
public class OutWeatherApi {

    private static Logger log = LoggerFactory.getLogger(OutWeatherApi.class);
    private final RestTemplate restTemplate;

    public WeatherDataResponseDto receivedFromWeatherApi(String lat, String lon) throws MalformedURLException, URISyntaxException {

        String urlRequest = createUrl(lat,lon);

        URL url = new URL(urlRequest);

        log.info("Sending request to {}", url);
        ResponseEntity<WeatherJSON> weatherJSON = restTemplate.getForEntity(url.toURI(), WeatherJSON.class);
        log.info("Received response {}", weatherJSON);

        if (weatherJSON.getBody() != null) {

            String cityFromResponse = weatherJSON.getBody().getData().get(0).getCity_name();
            String tempFromResponse = String.valueOf(Math.round(weatherJSON.getBody().getData().get(0).getTemp()));
            String appTempFromResponse = String.valueOf(Math.round(weatherJSON.getBody().getData().get(0).getApp_temp()));
            String descriptionFromResponse = weatherJSON.getBody().getData().get(0).getWeather().getDescription();
            String windFromResponse = String.format("%.1f", weatherJSON.getBody().getData().get(0).getWind_spd());
            String windDirFromResponse = weatherJSON.getBody().getData().get(0).getWind_cdir();
            String windDirFullFromResponse = weatherJSON.getBody().getData().get(0).getWind_cdir_full();
            String humidityFromResponse = "" + weatherJSON.getBody().getData().get(0).getRh();
            String iconFromResponse = weatherJSON.getBody().getData().get(0).getWeather().getIcon();

            return new WeatherDataResponseDto(lat,lon,cityFromResponse,tempFromResponse, appTempFromResponse,descriptionFromResponse,windFromResponse,windDirFromResponse, windDirFullFromResponse, humidityFromResponse, iconFromResponse);
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Response body not found");
        }
    }

    public MpWeatherDTO receivedFromWeatherMapApi(WeatherLatLonDTO position)  {
        String json1Response = restTemplate
                .getForObject(createUrl(position.getLat(), position.getLon()), String.class);
            ObjectMapper mapper = new ObjectMapper();

        JsonNode node;
         try{
           node =  mapper.readTree(json1Response);
        } catch (IOException e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Weather server is not available");
        }

        return new MpWeatherDTO(position.getCity(),
                Long.toString(Math.round(node.get("main").get("temp").asDouble())),
                Long.toString(Math.round(node.get("main").get("temp_max").asDouble())),
                Long.toString(Math.round(node.get("main").get("temp_min").asDouble())),
                node.get("weather").get(0).get("description").asText(),
                node.get("wind").get("speed").asText(),
                node.get("main").get("humidity").asText(),
                 node.get("weather").get(0).get("icon").asText());


    }

    private String createUrl(String lat, String lon) {

        //https://https://api.weatherbit.io/v2.0/current?lat=52.52453&lon=13.41004&key=37195ad08f4d48b98708b260b3747f6e&lang=de
        //https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${apiKey}&units=metric
        return UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat",lat)
                .queryParam("lon",lon)
                .queryParam("appid",System.getenv("WEATHERAPI"))
                .queryParam("units","metric")
                .queryParam("lang","de")
                .build()
                .toString();
    }
}
