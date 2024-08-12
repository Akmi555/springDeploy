package org.blb.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogsRequestDTO;

import org.blb.DTO.blog.blogs.BlogsResponseDTO;

import org.blb.DTO.mainPageDto.MpResponseDTO;
import org.blb.DTO.mainPageDto.MpWeatherDTO;
import org.blb.DTO.team.TeamResponse;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.team.Team;
import org.blb.repository.blog.BlogFindRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.team.TeamRepository;
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
    private final TeamRepository teamRepository;

    @GetMapping("/test")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> test(){
        teamRepository.deleteAll();
        teamRepository.save(new Team("Yevgen Filipchenko", "Team lead / Fullstack",
                "Main page, Authorization/Registration, Blogs","https://www.lerned.top/imj/kurses/YevgenFilipchenko.jpg",""));
        teamRepository.save(new Team("Max Openkin", "Frontend",
                "Blogs","https://www.lerned.top/imj/kurses/Max_Openkin.jpg",""));
        teamRepository.save(new Team("Ekaterina Bolvakina", "Backend",
                "News, Contacts, Weather","https://www.lerned.top/imj/kurses/Ekaterina_Bolvakina.jpg",""));
        teamRepository.save(new Team("Maria Romaniuk", "Frontend",
                "News, Contacts","https://www.lerned.top/imj/kurses/Maria_Romaniuk.jpg",""));
        teamRepository.save(new Team("Alina Klochai", "Backend",
                "Advertisement","https://www.lerned.top/imj/kurses/Alina_Klochai.jpg",""));
        teamRepository.save(new Team("Anna Christiansen", "Frontend",
                "Advertisement, Autorization/Registration","https://www.lerned.top/imj/kurses/Anna_Christiansen.jpg",""));
        teamRepository.save(new Team("Vatalii Chaplygin", "Backend",
                "Winnings","https://www.lerned.top/imj/kurses/Vatalii_Chaplygin.jpg",""));
        teamRepository.save(new Team("Viktor Tarlovsky", "Frontend",
                "Winnings","https://www.lerned.top/imj/kurses/Viktor_Tarlovsky.jpg",""));
        teamRepository.save(new Team("Olena Khvostykova", "Frontend",
                "Admin panel","https://www.lerned.top/imj/kurses/Olena_Khvostykova.jpg",""));

        return ResponseEntity.ok("ok");
    }

}
