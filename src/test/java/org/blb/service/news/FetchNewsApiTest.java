package org.blb.service.news;

import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.exeption.RestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchNewsApiTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FetchNewsApi fetchNewsApi;

    @BeforeEach
    void setUp() {
        reset(restTemplate);
    }

    @Test
    void testFetchDataFromApi_withValidResponse() {
        // Mock the API responses with specific URLs
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news", String.class)).thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=sport", String.class)).thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=wirtschaft", String.class)).thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=wissen", String.class)).thenReturn(getMockedJsonResponse());

        // Mock the details response
        when(restTemplate.getForObject("https://example.com/api2u/happy-news/sunshine-123.json", String.class)).thenReturn(getMockedDetailsJsonResponse());

        // Call the method and perform assertions
        Map<String, FetchNewsDataDTO> result = fetchNewsApi.fetchDataFromApi();
        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result map should not be empty");
        assertEquals(1, result.size(), "The result map should contain exactly one entry");

        // Validate the content
        FetchNewsDataDTO dto = result.get("Great News: Sunshine Returns!");
        assertNotNull(dto, "The DTO should not be null");
        assertEquals("Great News: Sunshine Returns!", dto.getTitle(), "The title should match");
        assertEquals("2024-08-10T14:18:32.344+02:00", dto.getDate(), "The date should match");
        assertNotNull(dto.getTitleImageSquare(), "The square image URL should not be null");
        assertNotNull(dto.getTitleImageWide(), "The wide image URL should not be null");
        assertNotNull(dto.getContent(), "The content should not be null");
    }

    @Test
    void testFetchDataFromApi_withEmptyResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");
        RestException exception = assertThrows(RestException.class, () -> fetchNewsApi.fetchDataFromApi());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus(), "Expected 500 INTERNAL_SERVER_ERROR status");
    }

    @Test
    void testFetchDataFromApi_withNullResponse() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);
        RestException exception = assertThrows(RestException.class, () -> fetchNewsApi.fetchDataFromApi());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    void testFetchContentFromDetailsUrl_withValidResponse() {
        // Mock the API responses with the expected data
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news", String.class))
                .thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=sport", String.class))
                .thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=wirtschaft", String.class))
                .thenReturn(getMockedJsonResponse());
        when(restTemplate.getForObject("https://www.tagesschau.de/api2u/news?ressort=wissen", String.class))
                .thenReturn(getMockedJsonResponse());

        // Mock the response for the details URL
        when(restTemplate.getForObject("https://example.com/api2u/happy-news/sunshine-123.json", String.class))
                .thenReturn(getMockedDetailsJsonResponse());

        // Call fetchDataFromApi to ensure the content is being fetched properly
        Map<String, FetchNewsDataDTO> result = fetchNewsApi.fetchDataFromApi();
        FetchNewsDataDTO dto = result.get("Great News: Sunshine Returns!");

        // Ensure that the DTO is correctly populated with the mocked details content
        assertNotNull(dto, "The DTO should not be null");
        String content = dto.getContent();
        assertNotNull(content, "The content should not be null");
        assertTrue(content.contains("Celebrate the return of sunny days"), "The content should contain 'Celebrate the return of sunny days'");
    }

    @Test
    void testFetchContentFromDetailsUrl_withInvalidJson() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("Invalid JSON");
        RestException exception = assertThrows(RestException.class, () -> fetchNewsApi.fetchDataFromApi());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }


    @Test
    void testFetchDataFromApi_withTimeout() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RestException(HttpStatus.GATEWAY_TIMEOUT, "Timeout"));

        RestException exception = assertThrows(RestException.class, () -> fetchNewsApi.fetchDataFromApi());
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, exception.getStatus());
    }

    @Test
    void testFetchDataFromApi_withUnexpectedJsonFormat() {
        // Mock the API response with unexpected JSON format
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{ \"unexpectedField\": \"value\" }");

        // Verify that an INTERNAL_SERVER_ERROR is thrown
        RestException exception = assertThrows(RestException.class, () -> fetchNewsApi.fetchDataFromApi());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus(), "Expected 500 INTERNAL_SERVER_ERROR status");
    }

    private String getMockedJsonResponse() {
        return """
        {
            "news": [
                {
                    "sophoraId": "happy-news-123",
                    "externalId": "123abc-456def-789ghi",
                    "title": "Great News: Sunshine Returns!",
                    "date": "2024-08-10T14:18:32.344+02:00",
                    "teaserImage": {
                        "alttext": "Happy people enjoying the sunshine",
                        "imageVariants": {
                            "1x1-144": "https://example.com/images/sunshine-1x1-144.jpg",
                            "1x1-840": "https://example.com/images/sunshine-1x1-840.jpg",
                            "16x9-960": "https://example.com/images/sunshine-16x9-960.jpg"
                        },
                        "type": "image"
                    },
                    "tags": [
                        {
                            "tag": "Sunshine"
                        },
                        {
                            "tag": "Joy"
                        },
                        {
                            "tag": "Happiness"
                        }
                    ],
                    "updateCheckUrl": "https://example.com/api2u/sunshine-123.json?view=hasChanged&lastKnown=abcd1234efgh5678ijkl9101",
                    "tracking": [
                        {
                            "sid": "app.happy.news.sunshine-123",
                            "src": "happytimes",
                            "ctp": "defined",
                            "pdt": "20240810T0935",
                            "otp": "news",
                            "cid": "sunshine-123",
                            "pti": "Great_News_Sunshine_Returns",
                            "bcr": "yes",
                            "type": "positive",
                            "av_full_show": false
                        }
                    ],
                    "topline": "Sunny Day",
                    "firstSentence": "The sun is shining bright, and everyone is feeling uplifted.",
                    "details": "https://example.com/api2u/happy-news/sunshine-123.json",
                    "detailsweb": "https://example.com/happy-news/sunshine-123.html",
                    "shareURL": "https://example.com/happy-news/sunshine-123.html",
                    "geotags": [],
                    "regionId": 0,
                    "regionIds": [],
                    "ressort": "happy",
                    "breakingNews": false,
                    "type": "story"
                }
            ],
            "regional": [],
            "newStoriesCountLink": "",
            "type": "news page",
            "nextPage": "https://example.com/api2u/news?date=240809"
        }
        """;
    }

    private String getMockedDetailsJsonResponse() {
        return """
           {
               "content": [
                   {
                       "value": "<strong>Celebrate the return of sunny days with joy and laughter!</strong>",
                       "type": "text"
                   }
               ]
           }
           """;
    }
}