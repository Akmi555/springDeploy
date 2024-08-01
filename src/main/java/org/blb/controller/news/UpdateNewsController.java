package org.blb.controller.news;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.news.NewsDataRequestDto;
import org.blb.controller.api.news.UpdateNewsApi;
import org.blb.service.news.UpdateNewsDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateNewsController implements UpdateNewsApi {
    private final UpdateNewsDataService updateNewsDataService;
    @Override
    public ResponseEntity<StandardResponseDto> updateReaction(@Valid @RequestBody NewsDataRequestDto dto) {
        updateNewsDataService.updateReaction(dto);
        return ResponseEntity.ok(new StandardResponseDto("Reaction for news with ID = "+ dto.getNewsId() +" updated successfully"));
    }
}
