package org.blb.service.util.newsCommentMapping;
import org.blb.DTO.newsComment.NewsCommentResponseDTO;
import org.blb.models.news.NewsComment;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsCommentConverterTest {

    @InjectMocks
    private NewsCommentConverter newsCommentConverter;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testToDto_Success() {
        String currentUserEmail = "currentUser@example.com";
        String commentAuthorEmail = "author@example.com";
        User user = new User();
        user.setEmail(commentAuthorEmail);
        user.setName("Author Name");

        NewsComment newsComment = new NewsComment();
        newsComment.setId(1L);
        newsComment.setComment("This is a comment");
        newsComment.setCommentDate(LocalDateTime.now());
        newsComment.setNewsDataEntity(new NewsDataEntity());
        newsComment.getNewsDataEntity().setId(2L);
        newsComment.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUserEmail);

        NewsCommentResponseDTO dto = newsCommentConverter.toDto(newsComment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("This is a comment", dto.getComment());
        assertEquals(2L, dto.getNewsId());
        assertEquals(newsComment.getCommentDate(), dto.getCommentDate());
        assertEquals("Author Name", dto.getAuthorName());
        assertFalse(dto.getIsPublishedByCurrentUser()); // `false` because the current user email is different from comment author email
    }

    @Test
    void testToDto_NoUser() {
        String currentUserEmail = "currentUser@example.com";

        NewsComment newsComment = new NewsComment();
        newsComment.setId(1L);
        newsComment.setComment("This is a comment");
        newsComment.setCommentDate(LocalDateTime.now());
        newsComment.setNewsDataEntity(new NewsDataEntity());
        newsComment.getNewsDataEntity().setId(2L);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(currentUserEmail);

        NewsCommentResponseDTO dto = newsCommentConverter.toDto(newsComment);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("This is a comment", dto.getComment());
        assertEquals(2L, dto.getNewsId());
        assertEquals(newsComment.getCommentDate(), dto.getCommentDate());
        assertNull(dto.getAuthorName()); // User is null, so authorName should be null
        assertFalse(dto.getIsPublishedByCurrentUser());
    }
}