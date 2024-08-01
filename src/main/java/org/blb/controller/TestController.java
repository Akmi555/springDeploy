package org.blb.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogsRequestDTO;

import org.blb.DTO.blog.blogs.BlogsResponseDTO;

import org.blb.DTO.mainPageDto.MpResponseDTO;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.blb.models.news.NewsDataEntity;
import org.blb.repository.blog.BlogFindRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.news.NewsDataRepository;
import org.blb.service.blog.BlogFindService;
import org.blb.service.mainPage.MainPageService;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@AllArgsConstructor
@Schema(hidden = true)
public class TestController {
    private final UserFindService userFindService;
    private final BlogRepository blogRepository;
    private final FindRegionService findRegionService;
    private final BlogFindRepository blogFindRepository;
    private final BlogFindService blogFindService;
    private final NewsDataRepository newsDataRepository;
    private final MainPageService mainPageService;

    @GetMapping("/test")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<MpResponseDTO> test(){


        return ResponseEntity.ok(mainPageService.getData());
    }

}
