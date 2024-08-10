package org.blb.service.news;

import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.models.news.NewsDataEntity;
import org.blb.repository.news.NewsDataRepository;
import org.blb.service.util.newsMapping.NewsDataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddNewsDataServiceTest {

    @Mock
    private FetchNewsApi fetchNewsApi;

    @Mock
    private NewsDataRepository newsDataRepository;

    @Mock
    private NewsDataConverter newsDataConverter;

    @InjectMocks
    private AddNewsDataService addNewsDataService;

    @BeforeEach
    void setUp() {
        reset(fetchNewsApi, newsDataRepository, newsDataConverter);
    }

    @Test
    void testSaveNewsFromFetchApi_withNewerNews() {
        FetchNewsDataDTO newsDataDTO = new FetchNewsDataDTO();
        newsDataDTO.setDate("2024-08-10T14:18:32.344+02:00");
        newsDataDTO.setTitle("Great News: Sunshine Returns!");
        Map<String, FetchNewsDataDTO> newsMap = new HashMap<>();
        newsMap.put("Great News: Sunshine Returns!", newsDataDTO);

        when(fetchNewsApi.fetchDataFromApi()).thenReturn(newsMap);
        when(newsDataRepository.findLastDate()).thenReturn(Optional.of("2024-08-09T00:00:00"));
        when(newsDataConverter.fromFetchApiToEntity(newsDataDTO)).thenReturn(new NewsDataEntity());

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, times(1)).save(any(NewsDataEntity.class));

        assertEquals("All news loaded successfully", response.getMessage());
    }

    @Test
    void testSaveNewsFromFetchApi_withNoNewNews() {
        FetchNewsDataDTO newsDataDTO = new FetchNewsDataDTO();
        newsDataDTO.setDate("2024-08-09T14:18:32.344+02:00"); // same or older date
        newsDataDTO.setTitle("Old News");
        Map<String, FetchNewsDataDTO> newsMap = new HashMap<>();
        newsMap.put("Old News", newsDataDTO);

        when(fetchNewsApi.fetchDataFromApi()).thenReturn(newsMap);
        when(newsDataRepository.findLastDate()).thenReturn(Optional.of("2024-08-09T14:18:32.344+02:00"));

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, never()).save(any(NewsDataEntity.class));

        assertEquals("All news loaded successfully", response.getMessage());
    }

    @Test
    void testSaveNewsFromFetchApi_withEmptyNews() {
        when(fetchNewsApi.fetchDataFromApi()).thenReturn(new HashMap<>());
        when(newsDataRepository.findLastDate()).thenReturn(Optional.of("2024-08-09T00:00:00"));

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, never()).save(any(NewsDataEntity.class));

        assertEquals("All news loaded successfully", response.getMessage());
    }

    @Test
    void testSaveNewsFromFetchApi_withNullDate() {
        FetchNewsDataDTO newsDataDTO = new FetchNewsDataDTO();
        newsDataDTO.setDate(null); // Null date
        newsDataDTO.setTitle("Null Date News");
        Map<String, FetchNewsDataDTO> newsMap = new HashMap<>();
        newsMap.put("Null Date News", newsDataDTO);

        when(fetchNewsApi.fetchDataFromApi()).thenReturn(newsMap);
        when(newsDataRepository.findLastDate()).thenReturn(Optional.of("2024-08-09T00:00:00"));

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, never()).save(any(NewsDataEntity.class));
        assertEquals("All news loaded successfully", response.getMessage());
    }

    @Test
    void testSaveNewsFromFetchApi_withInvalidDateFormat() {
        FetchNewsDataDTO newsDataDTO = new FetchNewsDataDTO();
        newsDataDTO.setDate("invalid-date-format"); // Invalid date format
        newsDataDTO.setTitle("Invalid Date Format News");
        Map<String, FetchNewsDataDTO> newsMap = new HashMap<>();
        newsMap.put("Invalid Date Format News", newsDataDTO);

        when(fetchNewsApi.fetchDataFromApi()).thenReturn(newsMap);
        when(newsDataRepository.findLastDate()).thenReturn(Optional.of("2024-08-09T00:00:00"));

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, never()).save(any(NewsDataEntity.class));
        assertEquals("All news loaded successfully", response.getMessage());
    }

    @Test
    void testSaveNewsFromFetchApi_withEmptyLastDate() {
        FetchNewsDataDTO newsDataDTO = new FetchNewsDataDTO();
        newsDataDTO.setDate("2024-08-10T14:18:32.344+02:00");
        newsDataDTO.setTitle("Recent News");
        Map<String, FetchNewsDataDTO> newsMap = new HashMap<>();
        newsMap.put("Recent News", newsDataDTO);

        NewsDataEntity newsDataEntity = new NewsDataEntity();
        when(fetchNewsApi.fetchDataFromApi()).thenReturn(newsMap);
        when(newsDataRepository.findLastDate()).thenReturn(Optional.empty()); // No last date
        when(newsDataConverter.fromFetchApiToEntity(newsDataDTO)).thenReturn(newsDataEntity);

        StandardResponseDto response = addNewsDataService.saveNewsFromFetchApi();

        verify(newsDataRepository, times(1)).save(newsDataEntity);
        assertEquals("All news loaded successfully", response.getMessage());
    }
}