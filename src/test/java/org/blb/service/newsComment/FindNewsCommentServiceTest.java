package org.blb.service.newsComment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.newsComment.NewsCommentResponseDTO;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsComment;
import org.blb.repository.news.NewsCommentRepository;
import org.blb.service.news.FindNewsDataService;
import org.blb.service.util.newsCommentMapping.NewsCommentConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FindNewsCommentServiceTest {

    @Mock
    private NewsCommentRepository newsCommentRepository;

    @Mock
    private NewsCommentConverter newsCommentConverter;

    @Mock
    private FindNewsDataService findNewsDataService;

    @InjectMocks
    private FindNewsCommentService findNewsCommentService;

    private NewsComment newsComment;
    private NewsCommentResponseDTO newsCommentResponseDTO;

    @BeforeEach
    void setUp() {
        newsComment = new NewsComment();
        newsComment.setId(1L);
        newsComment.setComment("Test comment message");
        newsComment.setCommentDate(LocalDateTime.parse("2024-07-26T13:30:00"));

        newsCommentResponseDTO = new NewsCommentResponseDTO();
        newsCommentResponseDTO.setId(1L);
        newsCommentResponseDTO.setComment("Test comment message");
        newsCommentResponseDTO.setCommentDate(LocalDateTime.parse("2024-07-26T13:30:00"));
    }

    @Test
    void testFindAllNewsComments_Success() {
        when(newsCommentRepository.findAll()).thenReturn(List.of(newsComment));
        when(newsCommentConverter.toDto(newsComment)).thenReturn(newsCommentResponseDTO);

        ResponseEntity<List<NewsCommentResponseDTO>> response = findNewsCommentService.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(newsCommentResponseDTO), response.getBody());
    }

    @Test
    void testFindById_Success() {
        when(newsCommentRepository.findById(1L)).thenReturn(Optional.of(newsComment));
        when(newsCommentConverter.toDto(newsComment)).thenReturn(newsCommentResponseDTO);

        ResponseEntity<NewsCommentResponseDTO> response = findNewsCommentService.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newsCommentResponseDTO, response.getBody());
    }

    @Test
    void testFindById_CommentNotFound() {
        when(newsCommentRepository.findById(1L)).thenReturn(Optional.empty());

        RestException thrown = assertThrows(RestException.class, () -> {
            findNewsCommentService.findById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("Comment with ID = 1 not found", thrown.getMessage());
    }

    @Test
    void testFindAllCommentsByNewsId_Success() {
        Long newsId = 1L;
        when(findNewsDataService.findNewsById(newsId)).thenReturn(null);
        when(newsCommentRepository.findAllByNewsDataEntityId(newsId)).thenReturn(List.of(newsComment));
        when(newsCommentConverter.toDto(newsComment)).thenReturn(newsCommentResponseDTO);

        ResponseEntity<List<NewsCommentResponseDTO>> response = findNewsCommentService.findAllCommentsByNewsId(newsId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(newsCommentResponseDTO), response.getBody());
    }

    @Test
    void testFindAllCommentsByNewsId_NewsNotFound() {
        Long newsId = 1L;
        doThrow(new RestException(HttpStatus.NOT_FOUND, "News with ID = " + newsId + " not found"))
                .when(findNewsDataService).findNewsById(newsId);

        RestException thrown = assertThrows(RestException.class, () -> {
            findNewsCommentService.findAllCommentsByNewsId(newsId);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("News with ID = " + newsId + " not found", thrown.getMessage());
    }
}