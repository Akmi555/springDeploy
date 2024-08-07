package org.blb.service.rent.productServise;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserFindService userFindService;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteProduct_Success() {
        // Arrange
        Long productId = 1L;
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(productId);
        product.setUser(user);

        when(userFindService.getUserFromContext()).thenReturn(user);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<OneMessageDTO> response = deleteProductService.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted successfully", response.getBody().getMessage());
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_NotFound() {
        // Arrange
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> deleteProductService.deleteProduct(productId));
        verify(productRepository, times(0)).deleteById(productId);
    }

    @Test
    void deleteProduct_NoPermission() {
        // Arrange
        Long productId = 1L;
        User currentUser = new User();
        currentUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Product product = new Product();
        product.setId(productId);
        product.setUser(otherUser);

        when(userFindService.getUserFromContext()).thenReturn(currentUser);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<OneMessageDTO> response = deleteProductService.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("You do not have permission to delete this product", response.getBody().getMessage());
        verify(productRepository, times(0)).deleteById(productId);
    }
}