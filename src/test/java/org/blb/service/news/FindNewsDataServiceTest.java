package org.blb.service.news;

import org.blb.DTO.news.NewsDataPageResponseDto;
import org.blb.DTO.news.NewsDataResponseDto;
import org.blb.exeption.NullArgException;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.region.Region;
import org.blb.repository.news.NewsDataRepository;
import org.blb.service.util.newsMapping.NewsDataConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindNewsDataServiceTest {
    @Mock
    private NewsDataRepository newsDataRepository;

    @Mock
    private NewsDataConverter newsDataConverter;

    @InjectMocks
    private FindNewsDataService findNewsDataService;

    private NewsDataEntity newsDataEntity;
    private NewsDataResponseDto newsDataResponseDto;

    @BeforeEach
    void setUp() {
        newsDataEntity = new NewsDataEntity();
        newsDataEntity.setId(1L);
        newsDataEntity.setRegion(new Region(8L, "Hessen"));
        newsDataEntity.setSectionName("inland");
        newsDataEntity.setTitle("Stromausfall in Bad Homburg: Tausende Haushalte und Bahnhof betroffen");
        newsDataEntity.setDate("2024-07-27T12:46:53.250+02:00");
        newsDataEntity.setTitleImageSquare("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlUSc/1x1-840.jpg");
        newsDataEntity.setTitleImageWide("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlNY8/16x9-960.jpg");
        newsDataEntity.setContent("<div className=\\\"textValueNews\\\"><strong>In Bad Homburg ist in der Nacht großflächig der Strom ausgefallen. Auch benachbarte Gemeinden waren zum Teil betroffen.</strong></div> ...");
        newsDataEntity.setLikeCount(10);
        newsDataEntity.setDislikeCount(5);
        newsDataEntity.setCommentsCount(1);

        newsDataResponseDto = new NewsDataResponseDto();
        newsDataResponseDto.setId(1L);
        newsDataResponseDto.setRegionId(8L);
        newsDataResponseDto.setRegionName("Hessen");
        newsDataResponseDto.setSectionName("inland");
        newsDataResponseDto.setTitle("Stromausfall in Bad Homburg: Tausende Haushalte und Bahnhof betroffen");
        newsDataResponseDto.setDate("2024-07-27T12:46:53.250+02:00");
        newsDataResponseDto.setTitleImageSquare("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlUSc/1x1-840.jpg");
        newsDataResponseDto.setTitleImageWide("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlNY8/16x9-960.jpg");
        newsDataResponseDto.setContent("<div className=\\\"textValueNews\\\"><strong>In Bad Homburg ist in der Nacht großflächig der Strom ausgefallen. Auch benachbarte Gemeinden waren zum Teil betroffen.</strong></div> ...");
        newsDataResponseDto.setLikeCount(10);
        newsDataResponseDto.setDislikeCount(5);
        newsDataResponseDto.setCommentsCount(1);
    }

    @Test
    void findAllNews() {
        when(newsDataRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(newsDataEntity)));
        when(newsDataConverter.fromEntityToDto(newsDataEntity))
                .thenReturn(newsDataResponseDto);

        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNews(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(1, response.getBody().getNewsDataPage().size());
        assertEquals(newsDataResponseDto, response.getBody().getNewsDataPage().get(0));
    }

    @Test
    void findAllNews_NoNewsFound() {
        when(newsDataRepository.findAll(any(PageRequest.class)))
                .thenReturn(Page.empty());

        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.findAllNews(0));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No News found", exception.getMessage());
    }

    @Test
    void findAllNews_InvalidPageNumber() {
        assertThrows(IllegalArgumentException.class, () -> findNewsDataService.findAllNews(-1));
    }

    @Test
    void findAllNews_MultipleNewsFound() {
        NewsDataEntity anotherNewsDataEntity = new NewsDataEntity();
        anotherNewsDataEntity.setId(2L);
        anotherNewsDataEntity.setRegion(new Region(9L, "Bayern"));
        anotherNewsDataEntity.setSectionName("welt");
        anotherNewsDataEntity.setTitle("Another News Title");
        anotherNewsDataEntity.setDate("2024-07-28T12:46:53.250+02:00");
        anotherNewsDataEntity.setTitleImageSquare("https://example.com/another-image-square.jpg");
        anotherNewsDataEntity.setTitleImageWide("https://example.com/another-image-wide.jpg");
        anotherNewsDataEntity.setContent("<div className=\\\"textValueNews\\\"><strong>Another content.</strong></div> ...");
        anotherNewsDataEntity.setLikeCount(20);
        anotherNewsDataEntity.setDislikeCount(3);
        anotherNewsDataEntity.setCommentsCount(4);

        NewsDataResponseDto anotherNewsDataResponseDto = new NewsDataResponseDto();
        anotherNewsDataResponseDto.setId(2L);
        anotherNewsDataResponseDto.setRegionId(9L);
        anotherNewsDataResponseDto.setRegionName("Bayern");
        anotherNewsDataResponseDto.setSectionName("welt");
        anotherNewsDataResponseDto.setTitle("Another News Title");
        anotherNewsDataResponseDto.setDate("2024-07-28T12:46:53.250+02:00");
        anotherNewsDataResponseDto.setTitleImageSquare("https://example.com/another-image-square.jpg");
        anotherNewsDataResponseDto.setTitleImageWide("https://example.com/another-image-wide.jpg");
        anotherNewsDataResponseDto.setContent("<div className=\\\"textValueNews\\\"><strong>Another content.</strong></div> ...");
        anotherNewsDataResponseDto.setLikeCount(20);
        anotherNewsDataResponseDto.setDislikeCount(3);
        anotherNewsDataResponseDto.setCommentsCount(4);

        when(newsDataRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(newsDataEntity, anotherNewsDataEntity)));
        when(newsDataConverter.fromEntityToDto(newsDataEntity))
                .thenReturn(newsDataResponseDto);
        when(newsDataConverter.fromEntityToDto(anotherNewsDataEntity))
                .thenReturn(anotherNewsDataResponseDto);

        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNews(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(2, response.getBody().getNewsDataPage().size());
    }

/// ================= findNewsById() ====================================================

    @Test
    void findNewsById() {
        Long id = 1L;
        when(newsDataRepository.findById(id)).thenReturn(Optional.of(newsDataEntity));
        when(newsDataConverter.fromEntityToDto(newsDataEntity)).thenReturn(newsDataResponseDto);

        ResponseEntity<NewsDataResponseDto> response = findNewsDataService.findNewsById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newsDataResponseDto, response.getBody());
    }

    @Test
    void findNewsById_NotFound() {
        Long id = 1L;
        when(newsDataRepository.findById(id)).thenReturn(Optional.empty());

        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.findNewsById(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News with ID = "+ id +" not found", exception.getMessage());
    }

/// ================= findAllNewsByRegionId() ====================================================

    @Test
    void findAllNewsByRegionId() {
        // Arrange
        Long regionId = 8L;
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id")));

        // Mock the page of entities
        Page<NewsDataEntity> mockedPage = new PageImpl<>(List.of(newsDataEntity));

        // Mock the repository to return the mocked page
        when(newsDataRepository.findByRegionId(regionId, pageRequest)).thenReturn(mockedPage);

        // Mock the converter to convert the entity to the response DTO
        when(newsDataConverter.fromEntityToDto(newsDataEntity)).thenReturn(newsDataResponseDto);

        // Act
        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNewsByRegionId(regionId, page);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount()); // Total pages
        assertEquals(0, response.getBody().getCurrentPage()); // Current page
        assertEquals(1, response.getBody().getNewsDataPage().size()); // Number of news items on this page
        assertEquals(newsDataResponseDto, response.getBody().getNewsDataPage().get(0)); // Verify the content
    }

    /// ================= findAllNewsByCriteria() ====================================================
    @Test
    void findAllNewsByCriteria_BothSectionAndRegionProvidedAndNewsFound() {
        String sectionName = "inland";
        String regionName = "Hessen";
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id")));

        Page<NewsDataEntity> mockedPage = new PageImpl<>(List.of(newsDataEntity));
        when(newsDataRepository.findBySectionNameAndRegionRegionName(sectionName, regionName, pageRequest)).thenReturn(mockedPage);
        when(newsDataConverter.fromEntityToDto(newsDataEntity)).thenReturn(newsDataResponseDto);

        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNewsByCriteria(sectionName, regionName, page);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(1, response.getBody().getNewsDataPage().size());
        assertEquals(newsDataResponseDto, response.getBody().getNewsDataPage().get(0));
    }

    @Test
    void findAllNewsByCriteria_OnlySectionProvidedAndNewsFound() {
        String sectionName = "inland";
        Integer page = 0;

        Page<NewsDataEntity> mockedPage = new PageImpl<>(List.of(newsDataEntity));
        when(newsDataRepository.findBySectionName(sectionName, PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id"))))).thenReturn(mockedPage);
        when(newsDataConverter.fromEntityToDto(newsDataEntity)).thenReturn(newsDataResponseDto);

        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNewsByCriteria(sectionName, null, page);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(1, response.getBody().getNewsDataPage().size());
        assertEquals(newsDataResponseDto, response.getBody().getNewsDataPage().get(0));
    }

    @Test
    void findAllNewsByCriteria_OnlyRegionProvidedAndNewsFound() {
        String regionName = "Hessen";
        Integer page = 0;

        Page<NewsDataEntity> mockedPage = new PageImpl<>(List.of(newsDataEntity));
        when(newsDataRepository.findByRegionRegionName(regionName, PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id"))))).thenReturn(mockedPage);
        when(newsDataConverter.fromEntityToDto(newsDataEntity)).thenReturn(newsDataResponseDto);

        ResponseEntity<NewsDataPageResponseDto> response = findNewsDataService.findAllNewsByCriteria(null, regionName, page);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getPageCount());
        assertEquals(0, response.getBody().getCurrentPage());
        assertEquals(1, response.getBody().getNewsDataPage().size());
        assertEquals(newsDataResponseDto, response.getBody().getNewsDataPage().get(0));
    }

    @Test
    void findAllNewsByCriteria_BothSectionAndRegionNotProvided() {
        Integer page = 0;

        NullArgException exception = assertThrows(NullArgException.class, () -> findNewsDataService.findAllNewsByCriteria(null, null, page));

        assertEquals("{\"fieldName\" : \"Both section and region name cannot be null or empty\", \"message\" : \"must not be null\"}", exception.getMessage());
    }
    @Test
    void findAllNewsByCriteria_BothSectionAndRegionProvidedAndNewsNotFound() {
        String sectionName = "sport";
        String regionName = "Hessen";
        Integer page = 0;
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id")));

        // Mock the repository to return an empty page
        Page<NewsDataEntity> emptyPage = Page.empty(pageRequest);
        when(newsDataRepository.findBySectionNameAndRegionRegionName(sectionName, regionName, pageRequest)).thenReturn(emptyPage);

        // Assert that a RestException is thrown
        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.findAllNewsByCriteria(sectionName, regionName, page));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News Data for section: '" + sectionName + "' and region: '" + regionName + "' not found", exception.getMessage());
    }
    @Test
    void findAllNewsByCriteria_OnlySectionProvidedAndNewsNotFound() {
        String sectionName = "kultur";
        Integer page = 0;

        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id")));

        // Mock the repository to return an empty page
        Page<NewsDataEntity> emptyPage = Page.empty(pageRequest);
        when(newsDataRepository.findBySectionName(sectionName, pageRequest)).thenReturn(emptyPage);

        // Assert that a RestException is thrown
        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.findAllNewsByCriteria(sectionName, null, page));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News Data for section name: '" + sectionName + "' not found", exception.getMessage());
    }

    @Test
    void findAllNewsByCriteria_OnlyRegionProvidedAndNewsNotFound() {
        String regionName = "Schweiz";
        Integer page = 0;

        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by(Sort.Order.desc("id")));

        // Mock the repository to return an empty page
        Page<NewsDataEntity> emptyPage = Page.empty(pageRequest);
        when(newsDataRepository.findByRegionRegionName(regionName, pageRequest)).thenReturn(emptyPage);

        // Assert that a RestException is thrown
        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.findAllNewsByCriteria(null, regionName, page));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News Data for region: '" + regionName + "' not found", exception.getMessage());
    }

    /// ================= findAllNewsByCriteria() ====================================================
    @Test
    void getNewsById_Found() {
        Long id = 1L;
        when(newsDataRepository.findById(id)).thenReturn(Optional.of(newsDataEntity));

        NewsDataEntity result = findNewsDataService.getNewsById(id);

        assertEquals(newsDataEntity, result);
    }

    @Test
    void getNewsById_NotFound() {
        Long id = 1L;
        when(newsDataRepository.findById(id)).thenReturn(Optional.empty());

        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.getNewsById(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News with ID = " + id + " not found", exception.getMessage());
    }

    @Test
    void getNewsById_InvalidID() {
        Long invalidId = -1L;  // Using an invalid ID value
        when(newsDataRepository.findById(invalidId)).thenReturn(Optional.empty());

        RestException exception = assertThrows(RestException.class, () -> findNewsDataService.getNewsById(invalidId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("News with ID = " + invalidId + " not found", exception.getMessage());
    }
}