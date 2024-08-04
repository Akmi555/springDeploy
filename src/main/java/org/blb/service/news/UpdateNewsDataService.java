package org.blb.service.news;

import lombok.AllArgsConstructor;
import org.blb.DTO.news.NewsDataRequestDto;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.news.NewsReaction;
import org.blb.models.user.User;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.news.NewsReactionRepository;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateNewsDataService {
    private final NewsDataRepository newsDataRepository;
    private final NewsReactionRepository newsReactionRepository;
    private final FindNewsDataService findNewsDataService;
    private final UserFindService userFindService;

    @Transactional
    public void updateReaction(NewsDataRequestDto dto) {
        User user = userFindService.getUserFromContext();
        NewsDataEntity newsData = findNewsDataService.getNewsById(dto.getNewsId());

        // Получить или создать новую реакцию
        NewsReaction reaction = newsReactionRepository.findByNewsDataAndUser(newsData, user)
                .orElse(new NewsReaction());

        int likeChange = 0;
        int dislikeChange = 0;

        if (dto.getLiked() && dto.getDisliked()) {
            if (reaction.getId() == null || (!reaction.getLiked() && !reaction.getDisliked())) {
                throw new RestException(HttpStatus.CONFLICT, "Cannot like and dislike the news simultaneously");
            }
        }

        if (reaction.getId() == null) {
            // Новая реакция
            reaction.setNewsData(newsData);
            reaction.setUser(user);
            reaction.setLiked(dto.getLiked());
            reaction.setDisliked(dto.getDisliked());
            if (dto.getLiked()) {
                likeChange = 1;
            }
            if (dto.getDisliked()) {
                dislikeChange = 1;
            }
        } else {
            // Существующая реакция
            if (dto.getLiked() && dto.getDisliked()) {
                if (reaction.getLiked()) {
                    // Сценарий 1: новость уже лайкнута, лайк убирается, дизлайк добавляется
                    likeChange = -1;
                    dislikeChange = 1;
                    reaction.setLiked(false);
                    reaction.setDisliked(true);
                } else if (reaction.getDisliked()) {
                    // Сценарий 2: новость уже дизлайкнута, дизлайк убирается, лайк добавляется
                    likeChange = 1;
                    dislikeChange = -1;
                    reaction.setLiked(true);
                    reaction.setDisliked(false);
                } else {
                    // Если новость не лайкнута и не дизлайкнута, устанавливаем значения из dto
                    reaction.setLiked(dto.getLiked());
                    reaction.setDisliked(dto.getDisliked());
                    if (dto.getLiked()) {
                        likeChange = 1;
                    }
                    if (dto.getDisliked()) {
                        dislikeChange = 1;
                    }
                }
            } else {
                if (dto.getLiked() && reaction.getLiked()) {
                    throw new RestException(HttpStatus.CONFLICT, "News already liked by this user");
                }
                if (dto.getDisliked() && reaction.getDisliked()) {
                    throw new RestException(HttpStatus.CONFLICT, "News already disliked by this user");
                }
                if (dto.getLiked()) {
                    if (reaction.getDisliked()) {
                        reaction.setDisliked(false);
                        dislikeChange = -1;
                    }
                    reaction.setLiked(true);
                    likeChange = 1;
                } else if (dto.getDisliked()) {
                    if (reaction.getLiked()) {
                        reaction.setLiked(false);
                        likeChange = -1;
                    }
                    reaction.setDisliked(true);
                    dislikeChange = 1;
                } else {
                    // Отмена существующей реакции
                    if (reaction.getLiked()) {
                        reaction.setLiked(false);
                        likeChange = -1;
                    } else if (reaction.getDisliked()) {
                        reaction.setDisliked(false);
                        dislikeChange = -1;
                    }
                }
            }
        }

        newsData.setLikeCount(newsData.getLikeCount() + likeChange);
        newsData.setDislikeCount(newsData.getDislikeCount() + dislikeChange);

        // Сохранить обновленную реакцию и данные о новости
        newsReactionRepository.save(reaction);
        newsDataRepository.save(newsData);
    }

    public void increaseCommentsCount(NewsDataEntity newsData) {
        newsData.setCommentsCount(newsData.getCommentsCount() + 1);
        newsDataRepository.save(newsData);
    }

    public void reduceCommentsCount(NewsDataEntity newsData) {
        newsData.setCommentsCount(newsData.getCommentsCount() - 1);
        newsDataRepository.save(newsData);
    }
}