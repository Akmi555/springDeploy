package org.blb.controller.news;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.controller.api.news.AddNewsApi;
import org.blb.service.news.AddNewsDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AddNewsController implements AddNewsApi {
    private final AddNewsDataService addNewsDataService;

    @Override
    @PostMapping("/news")
    public StandardResponseDto loadAllNewsFromAPIsToDataBase(){
        return addNewsDataService.saveNewsFromFetchApi();
    }
}
