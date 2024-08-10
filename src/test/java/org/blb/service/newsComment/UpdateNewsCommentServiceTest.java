package org.blb.service.newsComment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.newsComment.UpdateCommentRequestDTO;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsComment;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class UpdateNewsCommentServiceTest {

    @Mock
    private NewsCommentRepository newsCommentRepository;

    @Mock
    private UserFindService userFindService;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UpdateNewsCommentService updateNewsCommentService;

    private UpdateCommentRequestDTO dto;
    private User user;
    private NewsComment comment;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        dto = new UpdateCommentRequestDTO(1L, "Updated comment");
        user = new User();
        comment = new NewsComment();
        adminRole = new Role(1L, "ADMIN");
    }

    @Test
    void testUpdateNewsComment_Success() {
        user.setRole(adminRole);
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(newsCommentRepository.findById(dto.getId())).thenReturn(java.util.Optional.of(comment));
        when(roleRepository.findByRole("ADMIN")).thenReturn(adminRole);

        updateNewsCommentService.updateNewsComment(dto);

        verify(newsCommentRepository, times(1))
                .updateCommentById(dto.getComment(), LocalDateTime.now(), dto.getId());
    }

    @Test
    void testUpdateNewsComment_CommentNotFound() {
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(newsCommentRepository.findById(dto.getId())).thenReturn(java.util.Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            updateNewsCommentService.updateNewsComment(dto);
        });

        assertEquals("Comment with ID = " + dto.getId() + " not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).updateCommentById(anyString(), any(LocalDateTime.class), anyLong());
    }

    @Test
    void testUpdateNewsComment_InsufficientPermissions() {
        User regularUser = new User();
        regularUser.setRole(new Role(2L, "USER"));

        when(userFindService.getUserFromContext()).thenReturn(regularUser);
        when(newsCommentRepository.findById(dto.getId())).thenReturn(java.util.Optional.of(comment));
        when(roleRepository.findByRole("ADMIN")).thenReturn(adminRole);

        RestException thrown = assertThrows(RestException.class, () -> {
            updateNewsCommentService.updateNewsComment(dto);
        });

        assertEquals(HttpStatus.CONFLICT, thrown.getStatus());
        assertEquals("You don't have permission to update this comment", thrown.getMessage());
        verify(newsCommentRepository, times(0)).updateCommentById(anyString(), any(LocalDateTime.class), anyLong());
    }

    @Test
    void testUpdateNewsComment_UserNotFound() {
        when(userFindService.getUserFromContext()).thenThrow(new RestException(HttpStatus.FORBIDDEN, "User not found"));

        RestException thrown = assertThrows(RestException.class, () -> {
            updateNewsCommentService.updateNewsComment(dto);
        });

        assertEquals(HttpStatus.FORBIDDEN, thrown.getStatus());
        assertEquals("User not found", thrown.getMessage());
        verify(newsCommentRepository, times(0)).updateCommentById(anyString(), any(LocalDateTime.class), anyLong());
    }
}