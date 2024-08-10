package org.blb.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.weather.WeatherLatLonDTO;
import org.blb.exeption.RestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutGeoLocationApiTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OutGeoLocationApi outGeoLocationApi;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Test
    void testGetLatLonFromGeoLocation_Success() throws IOException {
        String ipAddress = "192.168.1.1";
        String jsonResponse = "{\"latitude\": \"40.7128\", \"longitude\": \"-74.0060\", \"city_name\": \"New York\"}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        WeatherLatLonDTO result = outGeoLocationApi.getLatLonFromGeoLocation(ipAddress);

        assertNotNull(result);
        assertEquals("40.7128", result.getLat());
        assertEquals("-74.0060", result.getLon());
        assertEquals("New York", result.getCity());
    }


    @Test
    void testGetLatLonFromGeoLocation_FalseApiKey() {
        String ipAddress = "192.168.1.1";

        // Mock the URL creation method to return the URL with an invalid API key
        OutGeoLocationApi spyOutGeoLocationApi = spy(outGeoLocationApi);
        doReturn("https://api.ip2location.io?key=B104FDB4918CSSF95FA32015337BC8A2&ip=" + ipAddress + "&format=json")
                .when(spyOutGeoLocationApi).createGeoLocationUrl(ipAddress);

        // Simulate an error response from the RestTemplate
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from URL: https://api.ip2location.io?key=B104FDB4918CSSF95FA32015337BC8A2&ip=192.168.1.1&format=json"));

        RestException thrown = assertThrows(RestException.class, () -> {
            spyOutGeoLocationApi.getLatLonFromGeoLocation(ipAddress);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
        assertEquals("Error fetching data from URL: https://api.ip2location.io?key=B104FDB4918CSSF95FA32015337BC8A2&ip=192.168.1.1&format=json", thrown.getMessage());
    }

    @Test
    void testGetLatLonFromGeoLocation_InvalidJson() throws IOException {
        String ipAddress = "192.168.1.1";
        String jsonResponse = "{invalid json}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        RestException thrown = assertThrows(RestException.class, () -> {
            outGeoLocationApi.getLatLonFromGeoLocation(ipAddress);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
        assertTrue(thrown.getMessage().contains("Error processing JSON response:"));
    }

    @Test
    void testGetLatLonFromGeoLocation_MissingData() throws IOException {
        String ipAddress = "192.168.1.1";
        String jsonResponse = "{\"city_name\": \"New York\"}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        RestException thrown = assertThrows(RestException.class, () -> {
            outGeoLocationApi.getLatLonFromGeoLocation(ipAddress);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("No geo-location data available", thrown.getMessage());
    }
}