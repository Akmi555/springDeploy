package org.blb.service.util.newsMapping;

import lombok.AllArgsConstructor;
import org.blb.DTO.news.NewsDataResponseDto;
import org.blb.DTO.news.newsJsonModel.FetchNewsDataDTO;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.news.NewsReaction;
import org.blb.models.region.Region;
import org.blb.models.user.User;
import org.blb.service.news.FindUserReactionServise;
import org.blb.service.news.UpdateNewsDataService;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class NewsDataConverter {
    private final FindRegionService findRegionService;
    private final UserFindService userFindService;
    private final FindUserReactionServise findUserReactionServise;
    public NewsDataResponseDto fromEntityToDto(NewsDataEntity newsDataEntity) {
        NewsDataResponseDto dto = new NewsDataResponseDto();
        Optional<User> user = userFindService.getUserFromContextIfPresent();
        Boolean like= false;
        Boolean dislike = false;
        if (user.isPresent()) {
            Optional<NewsReaction> reaction = findUserReactionServise.getNewsReactionByUser(newsDataEntity,user.get());
            if (reaction.isPresent()) {
                like = reaction.get().getLiked();
                dislike = reaction.get().getDisliked();
            }
        }



        dto.setId(newsDataEntity.getId());
        dto.setRegionId(newsDataEntity.getRegion().getId());
        dto.setRegionName(newsDataEntity.getRegion().getRegionName());
        dto.setSectionName(newsDataEntity.getSectionName());
        dto.setTitle(newsDataEntity.getTitle());
        dto.setDate(newsDataEntity.getDate().substring(0, 16).replace('T',' '));
        dto.setTitleImageSquare(newsDataEntity.getTitleImageSquare());
        dto.setTitleImageWide(newsDataEntity.getTitleImageWide());
        dto.setContent(newsDataEntity.getContent());
        dto.setLikeCount(newsDataEntity.getLikeCount());
        dto.setDislikeCount(newsDataEntity.getDislikeCount());
        dto.setCommentsCount(newsDataEntity.getCommentsCount());
        dto.setLike(like);
        dto.setDislike(dislike);
        return dto;
    }

    public NewsDataEntity fromFetchApiToEntity(FetchNewsDataDTO dto) {
        NewsDataEntity newsDataEntity = new NewsDataEntity();

        Region region = findRegionService.getRegionById(dto.getRegionId());
        newsDataEntity.setRegion(region);
        newsDataEntity.setSectionName(dto.getSectionName());
        newsDataEntity.setTitle(dto.getTitle());
        newsDataEntity.setDate(dto.getDate());
        newsDataEntity.setTitleImageSquare(dto.getTitleImageSquare());
        newsDataEntity.setTitleImageWide(dto.getTitleImageWide());
        newsDataEntity.setContent(dto.getContent());
        return newsDataEntity;
    }
}

