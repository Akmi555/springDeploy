package org.blb.controller.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.news.NewsDataPageResponseDto;
import org.blb.DTO.news.NewsDataResponseDto;
import org.blb.exeption.RestException;
import org.blb.service.news.FindNewsDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class FindNewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindNewsDataService findNewsDataService;

    @Autowired
    private ObjectMapper objectMapper;

    private NewsDataResponseDto newsDataResponseDto;
    private NewsDataPageResponseDto newsDataPageResponseDto;

    @BeforeEach
    void setUp() {
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

        newsDataPageResponseDto = new NewsDataPageResponseDto(1, 0, Collections.singletonList(newsDataResponseDto));
        newsDataPageResponseDto.setPageCount(1);
        newsDataPageResponseDto.setCurrentPage(0);
        newsDataPageResponseDto.setNewsDataPage(List.of(newsDataResponseDto));
    }

    @Test
    void testFindAllNews() throws Exception {
        when(findNewsDataService.findAllNews(anyInt())).thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        mockMvc.perform(get("/news")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }

    @Test
    void testFindNewsById() throws Exception {
        when(findNewsDataService.findNewsById(anyLong())).thenReturn(ResponseEntity.ok(newsDataResponseDto));

        mockMvc.perform(get("/news/{newsId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataResponseDto)));
    }

    @Test
    void testFindByCriteria() throws Exception {
        when(findNewsDataService.findAllNewsByCriteria(anyString(), anyString(), anyInt())).thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("section", "inland")
                        .param("region", "Hessen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }

    @Test
    void testFindAllNewsByRegionId() throws Exception {
        when(findNewsDataService.findAllNewsByRegionId(anyLong(), anyInt())).thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        mockMvc.perform(get("/news/region-id")
                        .param("page", "0")
                        .param("regionId", "8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }

    @Test
    void testFindNewsById_NotFound() throws Exception {
        when(findNewsDataService.findNewsById(anyLong())).thenThrow(new RestException(HttpStatus.NOT_FOUND, "News with ID = 1 not found"));

        mockMvc.perform(get("/news/{newsId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByCriteria_NotFound() throws Exception {
        when(findNewsDataService.findAllNewsByCriteria(anyString(), anyString(), anyInt()))
                .thenThrow(new RestException(HttpStatus.NOT_FOUND, "News Data for section: 'inland' and region: 'Hessen' not found"));

        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("section", "inland")
                        .param("region", "Hessen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void testFindAllNews_NotFound() throws Exception {
        when(findNewsDataService.findAllNews(anyInt())).thenThrow(new RestException(HttpStatus.NOT_FOUND, "No news found"));

        mockMvc.perform(get("/news")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllNewsByRegionId_NotFound() throws Exception {
        when(findNewsDataService.findAllNewsByRegionId(anyLong(), anyInt())).thenThrow(new RestException(HttpStatus.NOT_FOUND, "No news found for region ID = 8"));

        mockMvc.perform(get("/news/region-id")
                        .param("page", "0")
                        .param("regionId", "8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByCriteria_OnlySection() throws Exception {
        when(findNewsDataService.findAllNewsByCriteria(anyString(), isNull(), anyInt())).thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("section", "inland")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }

    @Test
    void testFindByCriteria_OnlyRegion() throws Exception {
        when(findNewsDataService.findAllNewsByCriteria(isNull(), anyString(), anyInt())).thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("region", "Hessen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }

    @Test
    void testFindByCriteria_OnlySection_NotFound() throws Exception {
        // Arrange
        when(findNewsDataService.findAllNewsByCriteria(eq("inland"), isNull(), anyInt()))
                .thenThrow(new RestException(HttpStatus.NOT_FOUND, "News Data for section name: 'inland' not found"));

        // Act & Assert
        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("section", "inland")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"News Data for section name: 'inland' not found\"}"));
    }

    @Test
    void testFindByCriteria_OnlyRegion_NotFound() throws Exception {
        // Arrange
        when(findNewsDataService.findAllNewsByCriteria(isNull(), eq("Hessen"), anyInt()))
                .thenThrow(new RestException(HttpStatus.NOT_FOUND, "News Data for region: 'Hessen' not found"));

        // Act & Assert
        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("region", "Hessen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"News Data for region: 'Hessen' not found\"}"));
    }

    @Test
    void testFindByCriteria_BothSectionAndRegionNull() throws Exception {
        // Arrange
        when(findNewsDataService.findAllNewsByCriteria(isNull(), isNull(), anyInt()))
                .thenThrow(new RestException(HttpStatus.BAD_REQUEST, "Both section and region name cannot be null or empty"));

        // Act & Assert
        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Both section and region name cannot be null or empty\"}"));
    }

    @Test
    void testFindByCriteria_ExceedinglyLongSection() throws Exception {
        // Arrange: Erstelle einen extrem langen String
        String longSectionName = new String(new char[10000]).replace("\0", "A"); // 10.000 Zeichen langer String

        when(findNewsDataService.findAllNewsByCriteria(eq(longSectionName), isNull(), anyInt()))
                .thenReturn(ResponseEntity.ok(newsDataPageResponseDto));

        // Act & Assert: Prüfe die Reaktion der API auf extrem lange Strings
        mockMvc.perform(get("/news/findBy")
                        .param("page", "0")
                        .param("section", longSectionName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newsDataPageResponseDto)));
    }
}