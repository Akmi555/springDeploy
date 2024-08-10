package org.blb.service.newsComment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.newsComment.NewsCommentRequestDTO;
import org.blb.models.news.NewsComment;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.service.news.FindNewsDataService;
import org.blb.service.news.UpdateNewsDataService;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.blb.exeption.RestException;

@ExtendWith(MockitoExtension.class)
class AddNewsCommentServiceTest {

    @Mock
    private NewsCommentRepository newsCommentRepository;

    @Mock
    private FindNewsDataService findNewsDataService;

    @Mock
    private UserFindService userFindService;

    @Mock
    private UpdateNewsDataService updateNewsDataService;

    @InjectMocks
    private AddNewsCommentService addNewsCommentService;

    private NewsCommentRequestDTO dto;
    private User user;
    private NewsDataEntity newsData;

    @BeforeEach
    void setUp() {
        dto = new NewsCommentRequestDTO("This is a comment", 1L);
        user = new User();
        newsData = new NewsDataEntity();
    }

    @Test
    void testAddNewsComment_Success() {
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);

        addNewsCommentService.addNewsComment(dto);

        verify(newsCommentRepository, times(1)).save(any(NewsComment.class));
        verify(updateNewsDataService, times(1)).increaseCommentsCount(newsData);
    }

    @Test
    void testAddNewsComment_UserNotFound() {
        when(userFindService.getUserFromContext()).thenThrow(new RestException(HttpStatus.FORBIDDEN, "User not found"));

        RestException thrown = assertThrows(RestException.class, () -> {
            addNewsCommentService.addNewsComment(dto);
        });

        assertEquals(HttpStatus.FORBIDDEN, thrown.getStatus());
        assertEquals("User not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).save(any(NewsComment.class));
        verify(updateNewsDataService, times(0)).increaseCommentsCount(any(NewsDataEntity.class));
    }

    @Test
    void testAddNewsComment_NewsDataNotFound() {
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenThrow(new RestException(HttpStatus.NOT_FOUND, "News data not found"));

        RestException thrown = assertThrows(RestException.class, () -> {
            addNewsCommentService.addNewsComment(dto);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("News data not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).save(any(NewsComment.class));
        verify(updateNewsDataService, times(0)).increaseCommentsCount(any(NewsDataEntity.class));
    }

    @Test
    void testAddNewsComment_SaveFail() {
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(findNewsDataService.getNewsById(dto.getNewsId())).thenReturn(newsData);
        doThrow(new RuntimeException("Save failed")).when(newsCommentRepository).save(any(NewsComment.class));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            addNewsCommentService.addNewsComment(dto);
        });

        assertEquals("Save failed", thrown.getMessage());
        verify(updateNewsDataService, times(0)).increaseCommentsCount(any(NewsDataEntity.class));
    }

}