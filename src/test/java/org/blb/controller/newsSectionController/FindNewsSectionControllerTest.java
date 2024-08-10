package org.blb.controller.newsSectionController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.news.NewsSectionDTO;
import org.blb.DTO.region.RegionDTO;
import org.blb.repository.news.NewsSectionRepository;
import org.blb.repository.region.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class FindNewsSectionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsSectionRepository newsSectionRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void testFindAllSections() throws Exception {
        List<NewsSectionDTO> sections = newsSectionRepository.findAll()
                .stream()
                .map(section -> new NewsSectionDTO(section.getId(), section.getSectionName()))
                .collect(Collectors.toList());

        mockMvc.perform(get("/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sections)));
    }
}