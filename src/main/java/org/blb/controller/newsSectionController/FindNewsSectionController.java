package org.blb.controller.newsSectionController;

import lombok.RequiredArgsConstructor;
import org.blb.DTO.news.NewsSectionDTO;
import org.blb.controller.api.newsSection.FindNewsSectionApi;
import org.blb.service.newsSection.FindNewsSectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class FindNewsSectionController implements FindNewsSectionApi {
    private final FindNewsSectionService service;

    @Override
    @GetMapping
    public ResponseEntity<List<NewsSectionDTO>> findAllNewsSections() {
        ResponseEntity<List<NewsSectionDTO>> response = service.findAllSections();
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
