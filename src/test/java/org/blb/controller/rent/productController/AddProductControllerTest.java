package org.blb.controller.rent.productController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.models.user.Role;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.rent.productServise.AddProductService;
import org.blb.service.user.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class AddProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private AddProductService addProductService;


    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    User testUser;

    @BeforeEach
    public void setUp() {

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

        Category category = new Category();
        category.setName("Saple Category");
        categoryRepository.save(category);

        Region region = new Region();
        region.setRegionName("Sample Region");
        regionRepository.save(region);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        regionRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void addNewProduct_WithImage_ShouldReturnOk() throws Exception {
        // Getting the user token
        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        // Create data for region and category
        RegionJustWithNameDto region = new RegionJustWithNameDto("Sample Region");
        CategoryCreateRequestDto category = new CategoryCreateRequestDto("Saple Category");

        // Create data for region and category
        ProductCreateRequestDto request = new ProductCreateRequestDto();
        request.setName("Sample Product");
        request.setDescription("Sample Description");
        request.setPrice(100.0);
        request.setCategory(category);
        request.setRegion(region);

        // Create MockMultipartFile for image
        MockMultipartFile image = new MockMultipartFile("image", "test.png", "image/png", "test image".getBytes());


        // Convert the object to JSON string
        String requestJson = new ObjectMapper().writeValueAsString(request);

        when(addProductService.addProduct(any(), any())).thenReturn(ResponseEntity.ok().build());

        // Execute the query with the correct parameters
        mockMvc.perform(multipart("/rent")
                        .file(image)
                        .file("product", requestJson.getBytes())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    void addNewProduct_WithoutImage_ShouldReturnOk() throws Exception {

        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        RegionJustWithNameDto region = new RegionJustWithNameDto("Sample Region");
        CategoryCreateRequestDto category = new CategoryCreateRequestDto("Saple Category");

        ProductCreateRequestDto request = new ProductCreateRequestDto();
        // Populate request with necessary data
        request.setName("Sample Product");
        request.setDescription("Sample Description");
        request.setPrice(100.0);
        request.setCategory(category);
        request.setRegion(region);
        request.setIsInStock(true);
        request.setImageUrl(null);
        // Add other properties as needed

        String requestJson = new ObjectMapper().writeValueAsString(request);

        when(addProductService.addProductWithoutImage(any())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/rent")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void addNewProduct_WithInvalidJson_ShouldReturnBadRequest() throws Exception {

        String token = userAuthService.authentication(testUser.getEmail(), "Qwerty1!").getToken();

        String invalidJson = "{ invalid json }"; // Invalid JSON

        mockMvc.perform(post("/rent")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist()); // Expect no response body
    }
}