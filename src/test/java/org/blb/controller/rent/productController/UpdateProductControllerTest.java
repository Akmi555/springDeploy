package org.blb.controller.rent.productController;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.rent.productServise.UpdateProductService;
import org.blb.service.user.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UpdateProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateProductService updateProductService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductCreateRequestDto productCreateRequestDto;

    private User testUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAuthService userAuthService;

    @BeforeEach
    void setUp() {


        userRepository.deleteAll();
        productRepository.deleteAll();
        regionRepository.deleteAll();
        categoryRepository.deleteAll();

        testUser = new User();
        Role role = roleRepository.findByRole("USER");
        testUser.setName("testUser2");
        testUser.setEmail("user2@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Qwerty1!"));
        testUser.setRole(role);
        testUser.setCode("");
        testUser.setState(State.CONFIRMED);
        userRepository.save(testUser);

        RegionJustWithNameDto region = new RegionJustWithNameDto("Sample Region");
        CategoryCreateRequestDto category = new CategoryCreateRequestDto("Sample Category");

        productCreateRequestDto = new ProductCreateRequestDto();
        productCreateRequestDto.setName("Sample Product");
        productCreateRequestDto.setDescription("Sample Description");
        productCreateRequestDto.setPrice(100.0);
        productCreateRequestDto.setCategory(category);
        productCreateRequestDto.setRegion(region);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        regionRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testUpdateProductSuccess() throws Exception {

        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        OneMessageDTO response = new OneMessageDTO("Product updated successfully");

        when(updateProductService.updateProduct(anyLong(), any(ProductCreateRequestDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(put("/rent/{id}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Product updated successfully\"}"));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {

        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        OneMessageDTO errorResponse = new OneMessageDTO("Product not found with id: 999");

        when(updateProductService.updateProduct(anyLong(), any(ProductCreateRequestDto.class)))
                .thenReturn(ResponseEntity.status(404).body(errorResponse));

        mockMvc.perform(put("/rent/{id}", 999L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Product not found with id: 999\"}"));
    }

    @Test
    void testUpdateProductCategoryNotFound() throws Exception {
        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        // Define the expected error response
        OneMessageDTO errorResponse = new OneMessageDTO("Category with name " + productCreateRequestDto.getCategory().getName() + " not found.");

        // Mock the service to return the error response when the category is not found
        when(updateProductService.updateProduct(anyLong(), any(ProductCreateRequestDto.class)))
                .thenReturn(ResponseEntity.status(404).body(errorResponse));

        // Perform the update product request
        mockMvc.perform(put("/rent/{id}", 1L) // You can use any valid product id here
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\", \"category\":{\"name\":\"Non-existent Category\"}}")) // Non-existent category name
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Category with name Sample Category not found.\"}")); // Update this to match the expected error message
    }

    @Test
    void testUpdateProductNoRights() throws Exception {
        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        // Define the expected error response
        OneMessageDTO errorResponse = new OneMessageDTO("You do not have the rights to update this product.");

        // Mock the service to return the error response when the user lacks permissions
        when(updateProductService.updateProduct(anyLong(), any(ProductCreateRequestDto.class)))
                .thenReturn(ResponseEntity.status(409).body(errorResponse)); // Using HttpStatus.CONFLICT

        // Perform the update product request
        mockMvc.perform(put("/rent/{id}", 1L) // Use a valid product id here
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\"}")) // Updated product details
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"message\":\"You do not have the rights to update this product.\"}"));
    }

    @Test
    void testUpdateProductRegionNotFound() throws Exception {
        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        // Define the expected error response
        OneMessageDTO errorResponse = new OneMessageDTO("Region with name " + productCreateRequestDto.getRegion().getRegionName() + " not found.");

        // Mock the service to return the error response when the region is not found
        when(updateProductService.updateProduct(anyLong(), any(ProductCreateRequestDto.class)))
                .thenReturn(ResponseEntity.status(404).body(errorResponse)); // Using HttpStatus.NOT_FOUND

        // Perform the update product request with a region that doesn't exist
        mockMvc.perform(put("/rent/{id}", 1L) // Use a valid product id here
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"description\":\"Updated Description\",\"region\":{\"regionName\":\"Non-existent Region\"}}")) // Updated product details
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Region with name Sample Region not found.\"}"));
    }
}