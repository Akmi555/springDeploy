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
        NewsDataEntity newsData = findNewsDataService.getNewsById(dto.getNewsId());
        User user = userFindService.getUserFromContext();

        // Fetch or create a new reaction
        NewsReaction reaction = newsReactionRepository.findByNewsDataAndUser(newsData, user)
                .orElse(new NewsReaction());

        if (reaction.getId() == null) {
            reaction.setNewsData(newsData);
            reaction.setUser(user);
        }

        // Prevent simultaneous like and dislike
        if ((dto.getLiked() != null && dto.getLiked()) && (dto.getDisliked() != null && dto.getDisliked())) {
            throw new RestException(HttpStatus.CONFLICT, "Cannot like and dislike the news simultaneously");
        }

        // Check if the action is redundant
        if (dto.getLiked() != null && dto.getLiked() && reaction.getLiked() != null && reaction.getLiked()) {
            throw new RestException(HttpStatus.CONFLICT, "News already liked by this user");
        }

        if (dto.getDisliked() != null && dto.getDisliked() && reaction.getDisliked() != null && reaction.getDisliked()) {
            throw new RestException(HttpStatus.CONFLICT, "News already disliked by this user");
        }

        // Adjust counts based on changes
        boolean isLikedChanged = dto.getLiked() != null && !dto.getLiked().equals(reaction.getLiked());
        boolean isDislikedChanged = dto.getDisliked() != null && !dto.getDisliked().equals(reaction.getDisliked());

        if (isLikedChanged) {
            if (dto.getLiked()) {
                // Increment like count if setting a new like
                newsData.setLikeCount(newsData.getLikeCount() + 1);
            } else if (reaction.getLiked() != null && reaction.getLiked()) {
                // Decrement like count if removing a like
                newsData.setLikeCount(newsData.getLikeCount() - 1);
            }
            reaction.setLiked(dto.getLiked());
        }

        if (isDislikedChanged) {
            if (dto.getDisliked()) {
                // Increment dislike count if setting a new dislike
                newsData.setDislikeCount(newsData.getDislikeCount() + 1);
            } else if (reaction.getDisliked() != null && reaction.getDisliked()) {
                // Decrement dislike count if removing a dislike
                newsData.setDislikeCount(newsData.getDislikeCount() - 1);
            }
            reaction.setDisliked(dto.getDisliked());
        }

        // Save the updated reaction and news data
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