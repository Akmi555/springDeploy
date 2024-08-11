package org.blb.controller.rent.productController;

import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.errorDto.FieldErrorDto;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.service.rent.productServise.FindProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FindProductControllerTest {

    @InjectMocks
    private FindProductController findProductController;

    @Mock
    private FindProductService findProductService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindProductsWithError() {
        // Create FieldErrorDto object
        FieldErrorDto fieldError = new FieldErrorDto("fieldName", "Field error message");

        // Creating a list fieldErrors
        List<FieldErrorDto> fieldErrors = Collections.singletonList(fieldError);

        // Create an error object with fields fieldErrors
        ErrorResponseDto error = new ErrorResponseDto("Error occurred", fieldErrors);

        // Create an empty product list and an error response object
        ProductSearchResponse response = new ProductSearchResponse(Collections.emptyList(), error, 0, 0);

        when(findProductService.findProducts(null, null, null, 0)).thenReturn(response);

        // Execute the controller method
        ResponseEntity<ProductSearchResponse> result = findProductController.findProducts(null, null, null, 0);

        // Check that the BAD_REQUEST status is returned
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindProductsSuccess() {
        // Create an answer object without an error
        ProductSearchResponse response = new ProductSearchResponse(Collections.emptyList(), null, 0, 1);

        when(findProductService.findProducts(null, null, null, 0)).thenReturn(response);

        // Execute the controller method
        ResponseEntity<ProductSearchResponse> result = findProductController.findProducts(null, null, null, 0);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

}