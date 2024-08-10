package org.blb.service.util.newsMapping;

import org.blb.DTO.news.NewsDataResponseDto;
import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.region.Region;
import org.blb.service.region.FindRegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsDataConverterTest {

    @InjectMocks
    private NewsDataConverter newsDataConverter;

    @Mock
    private FindRegionService findRegionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFromEntityToDto_Success() {
        Region region = new Region();
        region.setId(1L);
        region.setRegionName("Region1");

        NewsDataEntity newsDataEntity = new NewsDataEntity();
        newsDataEntity.setId(1L);
        newsDataEntity.setRegion(region);
        newsDataEntity.setSectionName("Section1");
        newsDataEntity.setTitle("Title");
        newsDataEntity.setDate("2024-08-10T10:00:00");
        newsDataEntity.setTitleImageSquare("squareImageUrl");
        newsDataEntity.setTitleImageWide("wideImageUrl");
        newsDataEntity.setContent("Content");
        newsDataEntity.setLikeCount(100);
        newsDataEntity.setDislikeCount(10);
        newsDataEntity.setCommentsCount(5);

        NewsDataResponseDto dto = newsDataConverter.fromEntityToDto(newsDataEntity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getRegionId());
        assertEquals("Region1", dto.getRegionName());
        assertEquals("Section1", dto.getSectionName());
        assertEquals("Title", dto.getTitle());
        assertEquals("2024-08-10 10:00", dto.getDate());
        assertEquals("squareImageUrl", dto.getTitleImageSquare());
        assertEquals("wideImageUrl", dto.getTitleImageWide());
        assertEquals("Content", dto.getContent());
        assertEquals(100, dto.getLikeCount());
        assertEquals(10, dto.getDislikeCount());
        assertEquals(5, dto.getCommentsCount());
    }

    @Test
    void testFromFetchApiToEntity_Success() {
        // Arrange
        FetchNewsDataDTO fetchNewsDataDTO = new FetchNewsDataDTO();
        fetchNewsDataDTO.setRegionId(1L);
        fetchNewsDataDTO.setSectionName("Section1");
        fetchNewsDataDTO.setTitle("Title");
        fetchNewsDataDTO.setDate("2024-08-10T10:00:00");
        fetchNewsDataDTO.setTitleImageSquare("squareImageUrl");
        fetchNewsDataDTO.setTitleImageWide("wideImageUrl");
        fetchNewsDataDTO.setContent("Content");

        Region region = new Region();
        region.setId(1L);
        when(findRegionService.getRegionById(1L)).thenReturn(region);

        NewsDataEntity newsDataEntity = newsDataConverter.fromFetchApiToEntity(fetchNewsDataDTO);

        assertNotNull(newsDataEntity);
        assertEquals(1L, newsDataEntity.getRegion().getId());
        assertEquals("Section1", newsDataEntity.getSectionName());
        assertEquals("Title", newsDataEntity.getTitle());
        assertEquals("2024-08-10T10:00:00", newsDataEntity.getDate());
        assertEquals("squareImageUrl", newsDataEntity.getTitleImageSquare());
        assertEquals("wideImageUrl", newsDataEntity.getTitleImageWide());
        assertEquals("Content", newsDataEntity.getContent());
    }

    @Test
    void testFromFetchApiToEntity_WithInvalidRegionId() {
        FetchNewsDataDTO fetchNewsDataDTO = new FetchNewsDataDTO();
        fetchNewsDataDTO.setRegionId(999L);
        fetchNewsDataDTO.setSectionName("Section1");
        fetchNewsDataDTO.setTitle("Title");
        fetchNewsDataDTO.setDate("2024-08-10T10:00:00");
        fetchNewsDataDTO.setTitleImageSquare("squareImageUrl");
        fetchNewsDataDTO.setTitleImageWide("wideImageUrl");
        fetchNewsDataDTO.setContent("Content");

        when(findRegionService.getRegionById(999L)).thenThrow(new RuntimeException("Region not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            newsDataConverter.fromFetchApiToEntity(fetchNewsDataDTO);
        });

        assertEquals("Region not found", exception.getMessage());
    }
}