package org.blb.controller.rent.productController;

import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.service.rent.productServise.AllProductsByUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class AllProductsByUserControllerTest {

    @InjectMocks
    private AllProductsByUserController allProductsByUserController;

    @Mock
    private AllProductsByUserService allProductsByUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProducts_ShouldReturnProducts_WhenValidPageNumber() {
        // Arrange
        int page = 1;
        ProductSearchResponse mockResponse = new ProductSearchResponse(
                List.of(new ProductResponseDto(/* field initialization  */)),
                null,
                page,
                10
        );

        when(allProductsByUserService.findUserProducts(anyInt())).thenReturn(mockResponse);

        // Act
        ResponseEntity<ProductSearchResponse> responseEntity = allProductsByUserController.getUserProducts(page);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    void getUserProducts_ShouldReturnEmptyResponse_WhenNoProducts() {
        // Arrange
        int page = 0;
        ProductSearchResponse mockResponse = new ProductSearchResponse(
                List.of(), // Пустой список продуктов
                null,
                page,
                0
        );

        when(allProductsByUserService.findUserProducts(anyInt())).thenReturn(mockResponse);

        // Act
        ResponseEntity<ProductSearchResponse> responseEntity = allProductsByUserController.getUserProducts(page);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    void getUserProducts_ShouldReturnErrorResponse_WhenErrorOccurs() {
        // Arrange
        int page = 2;
        ErrorResponseDto errorResponse = new ErrorResponseDto("Error occurred");
        ProductSearchResponse mockResponse = new ProductSearchResponse(
                null,
                errorResponse,
                page,
                0
        );

        when(allProductsByUserService.findUserProducts(anyInt())).thenReturn(mockResponse);

        // Act
        ResponseEntity<ProductSearchResponse> responseEntity = allProductsByUserController.getUserProducts(page);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
        assertEquals(errorResponse, responseEntity.getBody().getError());
    }
}