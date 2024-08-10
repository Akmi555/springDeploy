package org.blb.service.util.newsSectionMapping;
import org.blb.DTO.news.NewsSectionDTO;
import org.blb.models.news.NewsSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewsSectionConverterTest {

    private NewsSectionConverter newsSectionConverter;

    @BeforeEach
    void setUp() {
        newsSectionConverter = new NewsSectionConverter();
    }

    @Test
    void testToDTO_Success() {
        NewsSection newsSection = new NewsSection();
        newsSection.setId(1L);
        newsSection.setSectionName("SectionName");

        NewsSectionDTO dto = newsSectionConverter.toDTO(newsSection);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("SectionName", dto.getSectionName());
    }

    @Test
    void testToDTO_NullValues() {
        NewsSection newsSection = new NewsSection();
        newsSection.setId(null);
        newsSection.setSectionName(null);

        NewsSectionDTO dto = newsSectionConverter.toDTO(newsSection);

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getSectionName());
    }

    @Test
    void testToDTO_EmptySectionName() {
        NewsSection newsSection = new NewsSection();
        newsSection.setId(2L);
        newsSection.setSectionName("");

        NewsSectionDTO dto = newsSectionConverter.toDTO(newsSection);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("", dto.getSectionName());
    }
}