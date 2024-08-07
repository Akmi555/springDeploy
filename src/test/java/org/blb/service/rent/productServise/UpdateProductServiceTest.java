package org.blb.service.rent.productServise;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.region.RegionDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.region.FindRegionService;
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

class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserFindService userFindService;

    @Mock
    private FindRegionService findRegionService;

    @InjectMocks
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateProduct_Success() {
        // Arrange
        Long productId = 1L;
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setName("Electronics");

        RegionDTO regionDTO = new RegionDTO();
        regionDTO.setRegionName("California");

        Region region = new Region();
        region.setRegionName("California");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setUser(user);

        CategoryCreateRequestDto categoryDto = new CategoryCreateRequestDto("Electronics");

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setName("New Product Name");
        requestDto.setCategory(categoryDto);
        requestDto.setPrice(100.0);
        requestDto.setIsInStock(true);
        requestDto.setDescription("New Description");
        requestDto.setRegion(new RegionJustWithNameDto("California"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        when(findRegionService.findRegionByName("California")).thenReturn(regionDTO);

        // Act
        ResponseEntity<OneMessageDTO> response = updateProductService.updateProduct(productId, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your product has been successfully updated.", response.getBody().getMessage());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_ProductNotFound() {
        // Arrange
        Long productId = 1L;
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OneMessageDTO> response = updateProductService.updateProduct(productId, requestDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product with name " + productId + " not found.", response.getBody().getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_NoPermission() {
        // Arrange
        Long productId = 1L;
        User currentUser = new User();
        currentUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setUser(otherUser);

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userFindService.getUserFromContext()).thenReturn(currentUser);

        // Act
        ResponseEntity<OneMessageDTO> response = updateProductService.updateProduct(productId, requestDto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("You do not have the rights to update this product.", response.getBody().getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_CategoryNotFound() {
        // Arrange
        Long productId = 1L;
        User user = new User();
        user.setId(1L);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setUser(user);

        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto("NonExistentCategory");
        ProductCreateRequestDto productDto = new ProductCreateRequestDto();
        productDto.setCategory(requestDto);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(categoryRepository.findByName("NonExistentCategory")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OneMessageDTO> response = updateProductService.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category with name NonExistentCategory not found.", response.getBody().getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_RegionNotFound() {
        // Arrange
        Long productId = 1L;
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setName("Electronics");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setUser(user);

        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setRegion(new RegionJustWithNameDto("NonExistentRegion"));
        requestDto.setCategory(new CategoryCreateRequestDto("Electronics"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
        when(findRegionService.findRegionByName("NonExistentRegion")).thenReturn(null);

        // Act
        ResponseEntity<OneMessageDTO> response = updateProductService.updateProduct(productId, requestDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Region with name NonExistentRegion not found.", response.getBody().getMessage());
        verify(productRepository, times(0)).save(any(Product.class));
    }
}