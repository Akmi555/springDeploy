package org.blb.service.news;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.news.NewsDataRequestDto;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.news.NewsReaction;
import org.blb.models.user.User;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.news.NewsReactionRepository;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateNewsDataServiceTest {

    @Mock
    private NewsDataRepository newsDataRepository;

    @Mock
    private NewsReactionRepository newsReactionRepository;

    @Mock
    private FindNewsDataService findNewsDataService;

    @Mock
    private UserFindService userFindService;

    @InjectMocks
    private UpdateNewsDataService updateNewsDataService;

    private NewsDataEntity newsData;
    private User user;

    @BeforeEach
    void setUp() {
        newsData = new NewsDataEntity();
        newsData.setLikeCount(0);
        newsData.setDislikeCount(0);
        newsData.setCommentsCount(0);

        user = new User();
    }

    @Test
    void testUpdateReaction_like() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L,true,false);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        when(newsReactionRepository.findByNewsDataAndUser(newsData, user)).thenReturn(Optional.empty());

        updateNewsDataService.updateReaction(dto);

        assertEquals(1, newsData.getLikeCount());
        assertEquals(0, newsData.getDislikeCount());
        verify(newsReactionRepository, times(1)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testUpdateReaction_dislike() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L,false,true);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        when(newsReactionRepository.findByNewsDataAndUser(newsData, user)).thenReturn(Optional.empty());

        updateNewsDataService.updateReaction(dto);

        assertEquals(0, newsData.getLikeCount());
        assertEquals(1, newsData.getDislikeCount());
        verify(newsReactionRepository, times(1)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testUpdateReaction_likeAndDislike_conflict() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L,true,true);

        NewsReaction existingReaction = new NewsReaction();
        existingReaction.setLiked(false);
        existingReaction.setDisliked(false);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        when(newsReactionRepository.findByNewsDataAndUser(newsData, user)).thenReturn(Optional.of(existingReaction));

        RestException thrown = assertThrows(RestException.class, () -> {
            updateNewsDataService.updateReaction(dto);
        });

        assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
        assertEquals("Cannot like and dislike the news simultaneously", thrown.getMessage());
        verify(newsReactionRepository, times(0)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(0)).save(newsData);
    }

    @Test
    void testUpdateReaction_likeToDislike() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L,false,true);

        NewsReaction existingReaction = new NewsReaction();
        existingReaction.setLiked(true);
        existingReaction.setDisliked(false);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        when(newsReactionRepository.findByNewsDataAndUser(newsData, user)).thenReturn(Optional.of(existingReaction));

        updateNewsDataService.updateReaction(dto);

        assertEquals(0, newsData.getLikeCount()); // Like wird entfernt
        assertEquals(1, newsData.getDislikeCount()); // Dislike wird hinzugefügt
        verify(newsReactionRepository, times(1)).save(existingReaction);
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testUpdateReaction_dislikeToLike() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L,true,false);

        NewsReaction existingReaction = new NewsReaction();
        existingReaction.setLiked(false);
        existingReaction.setDisliked(true);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        when(newsReactionRepository.findByNewsDataAndUser(newsData, user)).thenReturn(Optional.of(existingReaction));

        updateNewsDataService.updateReaction(dto);

        assertEquals(1, newsData.getLikeCount()); // Like wird hinzugefügt
        assertEquals(0, newsData.getDislikeCount()); // Dislike wird entfernt
        verify(newsReactionRepository, times(1)).save(existingReaction);
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testUpdateReaction_userNotFound() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L, true, false);

        when(userFindService.getUserFromContext()).thenThrow(new RestException(HttpStatus.FORBIDDEN, "User must be registered to set a reaction for the news"));

        RestException thrown = assertThrows(RestException.class, () -> {
            updateNewsDataService.updateReaction(dto);
        });

        assertEquals(HttpStatus.FORBIDDEN, thrown.getStatus());
        assertEquals("User must be registered to set a reaction for the news", thrown.getMessage());
        verify(newsReactionRepository, times(0)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(0)).save(newsData);
    }

    @Test
    void testUpdateReaction_newsDataNotFound() {
        NewsDataRequestDto dto = new NewsDataRequestDto(1L, true, false);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenThrow(new RestException(HttpStatus.NOT_FOUND, "News with ID = " + dto.getNewsId() + " not found"));

        RestException thrown = assertThrows(RestException.class, () -> {
            updateNewsDataService.updateReaction(dto);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("News with ID = " + dto.getNewsId() + " not found", thrown.getMessage());
        verify(newsReactionRepository, times(0)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(0)).save(newsData);
    }

    @Test
    void testUpdateReaction_nullRequestDto() {
        NewsDataRequestDto dto = null;

        assertThrows(NullPointerException.class, () -> {
            updateNewsDataService.updateReaction(dto);
        });

        verify(newsReactionRepository, times(0)).save(any(NewsReaction.class));
        verify(newsDataRepository, times(0)).save(newsData);
    }


    @Test
    void testIncreaseCommentsCount() {
        updateNewsDataService.increaseCommentsCount(newsData);

        assertEquals(1, newsData.getCommentsCount());
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testReduceCommentsCount() {
        newsData.setCommentsCount(1);
        updateNewsDataService.reduceCommentsCount(newsData);

        assertEquals(0, newsData.getCommentsCount());
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testIncreaseCommentsCount_MultipleIncrements() {
        updateNewsDataService.increaseCommentsCount(newsData);
        updateNewsDataService.increaseCommentsCount(newsData);

        assertEquals(2, newsData.getCommentsCount());
        verify(newsDataRepository, times(2)).save(newsData);
    }

    @Test
    void testIncreaseCommentsCount_FromNonZero() {
        newsData.setCommentsCount(5);
        updateNewsDataService.increaseCommentsCount(newsData);

        assertEquals(6, newsData.getCommentsCount());
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testReduceCommentsCount_NonNegative() {
        newsData.setCommentsCount(0);
        updateNewsDataService.reduceCommentsCount(newsData);

        assertEquals(0, newsData.getCommentsCount()); // sollte nicht negativ werden
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testReduceCommentsCount_MultipleReductions() {
        newsData.setCommentsCount(3);
        updateNewsDataService.reduceCommentsCount(newsData);
        updateNewsDataService.reduceCommentsCount(newsData);

        assertEquals(1, newsData.getCommentsCount());
        verify(newsDataRepository, times(2)).save(newsData);
    }

    @Test
    void testReduceCommentsCount_FromNonZero() {
        newsData.setCommentsCount(5);
        updateNewsDataService.reduceCommentsCount(newsData);

        assertEquals(4, newsData.getCommentsCount());
        verify(newsDataRepository, times(1)).save(newsData);
    }

    @Test
    void testReduceCommentsCount_PreventNegativeValues() {
        newsData.setCommentsCount(5);
        updateNewsDataService.reduceCommentsCount(newsData); // Reduzierung um 1
        updateNewsDataService.reduceCommentsCount(newsData); // Weitere Reduzierung um 1
        updateNewsDataService.reduceCommentsCount(newsData); // Weitere Reduzierung um 1
        updateNewsDataService.reduceCommentsCount(newsData); // Weitere Reduzierung um 1
        updateNewsDataService.reduceCommentsCount(newsData); // Weitere Reduzierung um 1
        updateNewsDataService.reduceCommentsCount(newsData); // Sollte nicht weiter reduziert werden, da Wert 0

        assertEquals(0, newsData.getCommentsCount()); // Erwartet 0, da keine negativen Werte erlaubt sind
        verify(newsDataRepository, times(6)).save(newsData); // Methode sollte 6 Mal aufgerufen worden sein
    }
}