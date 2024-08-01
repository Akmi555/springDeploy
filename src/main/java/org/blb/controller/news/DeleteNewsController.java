package org.blb.controller.news;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.controller.api.news.DeleteNewsApi;
import org.blb.service.news.DeleteNewsDataService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class DeleteNewsController implements DeleteNewsApi {
    private final DeleteNewsDataService deleteNewsDataService;

    @Override
    @DeleteMapping("/news")
    public StandardResponseDto deleteNewsById(@Valid @RequestBody StandardDelRequest dto) {
        return deleteNewsDataService.deleteNewsDataById(dto.getId());
    }
}
