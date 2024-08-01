package org.blb.service.newsComment;

import lombok.AllArgsConstructor;
import org.blb.DTO.newsComment.NewsCommentRequestDTO;
import org.blb.models.news.NewsComment;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.service.news.FindNewsDataService;
import org.blb.service.news.UpdateNewsDataService;
import org.blb.service.user.UserFindService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AddNewsCommentService {
    private final NewsCommentRepository newsCommentRepository;
    private final FindNewsDataService findNewsDataService;
    private final UserFindService userFindService;
    private final UpdateNewsDataService updateNewsDataService;

    public void addNewsComment(NewsCommentRequestDTO DTO) {
        User user = userFindService.getUserFromContext();
        NewsDataEntity newsData = findNewsDataService.getNewsById(DTO.getNewsId());
        newsCommentRepository.save(new NewsComment(DTO.getComment(), LocalDateTime.now(), user, newsData));

        updateNewsDataService.increaseCommentsCount(newsData);
    }
}
