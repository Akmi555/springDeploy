package org.blb.service.rent.productServise;

import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.errorDto.FieldErrorDto;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.rent.categoryService.FindCategoryService;
import org.blb.service.util.rentMapping.ProductConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;



class FindProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private FindRegionService findRegionService;

    @Mock
    private FindCategoryService findCategoryService;

    @InjectMocks
    private FindProductService findProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findProducts_whenNoCriteriaProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> productList = new ArrayList<>();
        Page<Product> productPage = new PageImpl<>(productList, pageable, 0);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        ProductSearchResponse response = findProductService.findProducts(null, null, null, 0);

        assertEquals(0, response.getTotalPages());
        assertEquals(0, response.getTotalPages());
    }

    @Test
    void findProducts_whenRegionNotFound() {
        String regionName = "NonExistentRegion";
        when(findRegionService.findRegionByNameOptional(regionName)).thenReturn(Optional.empty());

        ProductSearchResponse response = findProductService.findProducts(regionName, null, null, 0);

        assertEquals(1, response.getError().getFieldErrors().size());
        assertEquals("region", response.getError().getFieldErrors().get(0).getField());
    }

    @Test
    void findProducts_whenCategoryNotFound() {
        String categoryName = "NonExistentCategory";
        when(findCategoryService.findByName(categoryName)).thenReturn(Optional.empty());

        ProductSearchResponse response = findProductService.findProducts(null, categoryName, null, 0);

        assertEquals(1, response.getError().getFieldErrors().size());
        assertEquals("category", response.getError().getFieldErrors().get(0).getField());
    }

    @Test
    void findProductById_whenProductNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> findProductService.findProductById(productId));
    }

    @Test
    void findProductById_whenProductFound() {
        Long productId = 1L;
        Product product = new Product(); // Предположим, у вас есть конструктор по умолчанию
        ProductResponseDto productDto = new ProductResponseDto(); // И аналогичный DTO

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productConverter.toDto(product)).thenReturn(productDto);

        ProductResponseDto response = findProductService.findProductById(productId);

        assertEquals(productDto, response);
    }
}