package org.blb.service.newsSection;

import org.blb.DTO.news.NewsSectionDTO;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsSection;
import org.blb.repository.news.NewsSectionRepository;
import org.blb.service.util.newsSectionMapping.NewsSectionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindNewsSectionServiceTest {

    @InjectMocks
    private FindNewsSectionService findNewsSectionService;

    @Mock
    private NewsSectionRepository newsSectionRepository;

    @Mock
    private NewsSectionConverter newsSectionConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllSections_Success() {
        NewsSection newsSection = new NewsSection();
        newsSection.setId(1L);
        newsSection.setSectionName("Section1");

        NewsSectionDTO newsSectionDTO = new NewsSectionDTO();
        newsSectionDTO.setId(1L);
        newsSectionDTO.setSectionName("Section1");

        when(newsSectionRepository.findAll()).thenReturn(Collections.singletonList(newsSection));
        when(newsSectionConverter.toDTO(newsSection)).thenReturn(newsSectionDTO);

        ResponseEntity<List<NewsSectionDTO>> response = findNewsSectionService.findAllSections();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Section1", response.getBody().get(0).getSectionName());
    }

    @Test
    void testFindAllSections_EmptyList() {
        when(newsSectionRepository.findAll()).thenReturn(Collections.emptyList());

        RestException thrown = assertThrows(RestException.class, () -> {
            findNewsSectionService.findAllSections();
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("No news sections found", thrown.getMessage());
    }
}