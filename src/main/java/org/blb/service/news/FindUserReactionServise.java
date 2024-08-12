package org.blb.service.news;

import lombok.AllArgsConstructor;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.news.NewsReaction;
import org.blb.models.user.User;
import org.blb.repository.news.NewsReactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FindUserReactionServise {
    private final NewsReactionRepository newsReactionRepository;
    public Optional<NewsReaction> getNewsReactionByUser(NewsDataEntity newsData, User user) {
        return newsReactionRepository.findByNewsDataAndUser(newsData, user);
    }
}
