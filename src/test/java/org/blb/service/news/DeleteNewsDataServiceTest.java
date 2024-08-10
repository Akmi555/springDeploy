package org.blb.service.news;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.exeption.RestException;
import org.blb.repository.news.NewsDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class DeleteNewsDataServiceTest {

    @Mock
    private NewsDataRepository newsDataRepository;

    @InjectMocks
    private DeleteNewsDataService deleteNewsDataService;

    @BeforeEach
    void setUp() {
        reset(newsDataRepository);
    }

    @Test
    void testDeleteNewsDataById_existingId() {
        Long existingId = 1L;
        when(newsDataRepository.existsById(existingId)).thenReturn(true);
        StandardResponseDto response = deleteNewsDataService.deleteNewsDataById(existingId);

        verify(newsDataRepository, times(1)).deleteById(existingId);
        verify(newsDataRepository, times(1)).existsById(existingId);

        assertEquals("News with ID = " + existingId + " deleted successfully", response.getMessage());
    }

    @Test
    void testDeleteNewsDataById_nonExistingId() {
        Long nonExistingId = 999L;
        when(newsDataRepository.existsById(nonExistingId)).thenReturn(false);

        RestException thrown = assertThrows(RestException.class, () -> {
            deleteNewsDataService.deleteNewsDataById(nonExistingId);
        });

        verify(newsDataRepository, times(0)).deleteById(nonExistingId);
        verify(newsDataRepository, times(1)).existsById(nonExistingId);

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        assertEquals("News with ID = " + nonExistingId + " not found", thrown.getMessage());
    }

    @Test
    void testDeleteNewsDataById_nullId() {
        Long nullId = null;
        RestException thrown = assertThrows(RestException.class, () -> {
            deleteNewsDataService.deleteNewsDataById(nullId);
        });

        verify(newsDataRepository, times(0)).deleteById(nullId);
        verify(newsDataRepository, times(0)).existsById(nullId);

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Invalid ID: null", thrown.getMessage());
    }

    @Test
    void testDeleteNewsDataById_deleteThrowsException() {
        Long existingId = 1L;
        when(newsDataRepository.existsById(existingId)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(newsDataRepository).deleteById(existingId);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            deleteNewsDataService.deleteNewsDataById(existingId);
        });

        verify(newsDataRepository, times(1)).deleteById(existingId);
        verify(newsDataRepository, times(1)).existsById(existingId);

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void testDeleteNewsDataById_negativeId() {
        Long negativeId = -1L;

        RestException thrown = assertThrows(RestException.class, () -> {
            deleteNewsDataService.deleteNewsDataById(negativeId);
        });

        verify(newsDataRepository, times(0)).deleteById(negativeId);
        verify(newsDataRepository, times(0)).existsById(negativeId);

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
        assertEquals("Invalid ID: -1", thrown.getMessage());
    }
}