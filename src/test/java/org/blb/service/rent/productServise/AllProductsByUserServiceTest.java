package org.blb.service.rent.productServise;

import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
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
import org.springframework.data.domain.Sort;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AllProductsByUserServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserFindService userFindService;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private AllProductsByUserService allProductsByUserService;

    private User user;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void findUserProducts_Success() {
        Product product = new Product();
        product.setId(1L);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(1L);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(productRepository.findAllByUser(user, pageable)).thenReturn(productPage);
        when(productConverter.toDto(product)).thenReturn(productResponseDto);

        ProductSearchResponse response = allProductsByUserService.findUserProducts(0);

        assertNotNull(response);
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getTotalPages());
        assertEquals(1, response.getProducts().size());
        assertEquals(1L, response.getProducts().get(0).getId());
    }

    @Test
    void findUserProducts_NoProductsFound() {
        Page<Product> emptyProductPage = new PageImpl<>(Collections.emptyList());

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(productRepository.findAllByUser(user, pageable)).thenReturn(emptyProductPage);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            allProductsByUserService.findUserProducts(0);
        });

        assertEquals("You have not products yet.", exception.getMessage());
    }
}