package org.blb.service.rent.productServise;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.validationErrorDto.ValidationErrorDto;
import org.blb.DTO.validationErrorDto.ValidationErrorsDto;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
import org.blb.service.util.rentMapping.ProductConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductConverter productConverter;

    @Mock
    private UserFindService userFindService;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private SupabaseService supabaseService;

    @InjectMocks
    private AddProductService addProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_Success() throws Exception {
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setCategory(new CategoryCreateRequestDto("CategoryName"));
        requestDto.setRegion(new RegionJustWithNameDto("RegionName"));
        requestDto.setDescription("Product Description");
        MultipartFile image = mock(MultipartFile.class);

        Category category = new Category();
        Region region = new Region();
        User user = new User();
        Product product = new Product();
        String imageUrl = "http://image.url";

        when(categoryRepository.findByName("CategoryName")).thenReturn(Optional.of(category));
        when(regionRepository.findByRegionName("RegionName")).thenReturn(Optional.of(region));
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(productConverter.fromDto(requestDto)).thenReturn(product);
        when(supabaseService.uploadImage(image)).thenReturn(imageUrl);

        ResponseEntity<?> response = addProductService.addProduct(requestDto, image);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product successfully created", ((OneMessageDTO) response.getBody()).getMessage());
        verify(productRepository, times(1)).save(product);
        assertEquals(user, product.getUser());
        assertEquals(category, product.getCategory());
        assertEquals(region, product.getRegion());
        assertEquals("Product Description", product.getDescription());
        assertEquals(imageUrl, product.getLink());
    }

    @Test
    void addProduct_ValidationErrors() {
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        MultipartFile image = mock(MultipartFile.class);

        ResponseEntity<?> response = addProductService.addProduct(requestDto, image);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ValidationErrorsDto errors = (ValidationErrorsDto) response.getBody();
        assertNotNull(errors);
        assertFalse(errors.getErrors().isEmpty());
    }

    @Test
    void addProductWithoutImage_Success() {
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setCategory(new CategoryCreateRequestDto("CategoryName"));
        requestDto.setRegion(new RegionJustWithNameDto("RegionName"));
        requestDto.setDescription("Product Description");

        Category category = new Category();
        Region region = new Region();
        User user = new User();
        Product product = new Product();

        when(categoryRepository.findByName("CategoryName")).thenReturn(Optional.of(category));
        when(regionRepository.findByRegionName("RegionName")).thenReturn(Optional.of(region));
        when(userFindService.getUserFromContext()).thenReturn(user);
        when(productConverter.fromDto(requestDto)).thenReturn(product);

        ResponseEntity<?> response = addProductService.addProductWithoutImage(requestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product successfully created", ((OneMessageDTO) response.getBody()).getMessage());
        verify(productRepository, times(1)).save(product);
        assertEquals(user, product.getUser());
        assertEquals(category, product.getCategory());
        assertEquals(region, product.getRegion());
        assertEquals("Product Description", product.getDescription());
    }

    @Test
    void addProductWithoutImage_ValidationErrors() {
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();

        ResponseEntity<?> response = addProductService.addProductWithoutImage(requestDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ValidationErrorsDto errors = (ValidationErrorsDto) response.getBody();
        assertNotNull(errors);
        assertFalse(errors.getErrors().isEmpty());
    }
}