package org.blb.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.exeption.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@AllArgsConstructor
public class OutGeoLocationApi {
    private static Logger log = LoggerFactory.getLogger(OutGeoLocationApi.class);
    private final RestTemplate restTemplate;

    public WeatherLatLonDTO getLatLonFromGeoLocation(String ipAddress) {

        log.info("user Ip Address - "+ipAddress);
        String urlGeo = createGeoLocationUrl(ipAddress);

        log.info("Sending request to {}", urlGeo);

        String json = restTemplate.getForObject(urlGeo, String.class);
        log.info("Raw JSON response: {}", json);

        if (json == null) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from URL: " + urlGeo);
        }

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonResponse;
        try {
            jsonResponse = mapper.readTree(json);
        } catch (IOException e) {
            throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON response: " + e.getMessage());
        }

        // Extract latitude and longitude from JSON response
        if (jsonResponse.has("latitude") && jsonResponse.has("longitude")) {
            String latFromResponse = jsonResponse.get("latitude").asText();
            String lonFromResponse = jsonResponse.get("longitude").asText();
            return new WeatherLatLonDTO(latFromResponse, lonFromResponse,
                    jsonResponse.get("city_name").asText());
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "No geo-location data available");
        }
    }

    String createGeoLocationUrl(String ipAddress) {
        return UriComponentsBuilder.fromHttpUrl("https://api.ip2location.io")
                .queryParam("key", System.getenv("GEOAPI"))
                .queryParam("ip", ipAddress)
                .queryParam("format", "json")
                .build()
                .toString();
    }
}