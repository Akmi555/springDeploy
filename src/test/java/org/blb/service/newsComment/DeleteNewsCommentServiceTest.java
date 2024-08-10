package org.blb.service.newsComment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsComment;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.service.news.UpdateNewsDataService;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DeleteNewsCommentServiceTest {

    @Mock
    private NewsCommentRepository newsCommentRepository;

    @Mock
    private UpdateNewsDataService updateNewsDataService;

    @Mock
    private UserFindService userFindService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private DeleteNewsCommentService deleteNewsCommentService;

    private User adminUser;
    private User regularUser;
    private NewsComment comment;

    @BeforeEach
    void setUp() {
        adminUser = new User();
        adminUser.setRole(new Role(1L,"ADMIN"));

        regularUser = new User();
        regularUser.setRole(new Role(2L,"USER"));

        comment = new NewsComment();
        comment.setUser(regularUser);
        comment.setId(1L);
    }

    @Test
    void testDeleteNewsComment_Success() {
        when(userFindService.getUserFromContext()).thenReturn(regularUser);
        when(newsCommentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));
        when(roleRepository.findByRole("ADMIN")).thenReturn(adminUser.getRole());

        deleteNewsCommentService.deleteNewsCommentById(new StandardDelRequest(1L));

        verify(newsCommentRepository, times(1)).delete(comment);
        verify(updateNewsDataService, times(1)).reduceCommentsCount(comment.getNewsDataEntity());
    }

    @Test
    void testDeleteNewsComment_UserNotFound() {
        when(userFindService.getUserFromContext()).thenThrow(new RestException(HttpStatus.FORBIDDEN, "User not found"));

        RestException thrown = assertThrows(RestException.class, () -> {
            deleteNewsCommentService.deleteNewsCommentById(new StandardDelRequest(1L));
        });

        assertEquals(HttpStatus.FORBIDDEN, thrown.getStatus());
        assertEquals("User not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).delete(any(NewsComment.class));
    }

    @Test
    void testDeleteNewsComment_CommentNotFound() {
        when(userFindService.getUserFromContext()).thenReturn(regularUser);
        when(newsCommentRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            deleteNewsCommentService.deleteNewsCommentById(new StandardDelRequest(1L));
        });

        assertEquals("Comment with id = 1 not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).delete(any(NewsComment.class));
    }

    @Test
    void testDeleteNewsComment_CommentNotAssociatedWithUser() {
        User differentUser = new User();
        differentUser.setRole(new Role(2L,"USER"));

        when(userFindService.getUserFromContext()).thenReturn(differentUser);
        when(newsCommentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));
        when(roleRepository.findByRole("ADMIN")).thenReturn(new Role(1L, "ADMIN"));

        RestException thrown = assertThrows(RestException.class, () -> {
            deleteNewsCommentService.deleteNewsCommentById(new StandardDelRequest(1L));
        });

        assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
        assertEquals("You don't have permission to delete this comment", thrown.getMessage());
        verify(newsCommentRepository, times(0)).delete(any(NewsComment.class));
        verify(updateNewsDataService, times(0)).reduceCommentsCount(any());
    }
}