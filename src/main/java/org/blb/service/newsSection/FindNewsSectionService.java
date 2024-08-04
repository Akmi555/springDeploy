package org.blb.service.newsSection;

import lombok.AllArgsConstructor;
import org.blb.DTO.news.NewsSectionDTO;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsSection;
import org.blb.repository.news.NewsSectionRepository;
import org.blb.service.util.newsSectionMapping.NewsSectionConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindNewsSectionService {

    private final NewsSectionRepository newsSectionRepository;
    private final NewsSectionConverter newsSectionConverter;

    public ResponseEntity<List<NewsSectionDTO>> findAllSections() {
        List<NewsSection> sections = newsSectionRepository.findAll();
        List<NewsSectionDTO> DTOs = sections.stream().map(newsSectionConverter::toDTO).toList();
        if (!sections.isEmpty()) {
            return new ResponseEntity<>(DTOs, HttpStatus.OK);
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "No news sections found");
        }
    }
}
