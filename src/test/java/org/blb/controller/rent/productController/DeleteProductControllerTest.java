package org.blb.controller.rent.productController;


import jakarta.transaction.Transactional;
import org.blb.models.rent.Product;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.rent.ProductRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.user.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class DeleteProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ProductRepository productRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = createUser("testUser", "user@gmail.com", "USER");
        product = createProduct("Test Product", 100.0, user);
    }

    private User createUser(String name, String email, String roleName) {
        User user = new User();
        Role role = roleRepository.findByRole(roleName);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Qwerty1!"));
        user.setRole(role);
        user.setCode("");
        user.setState(State.CONFIRMED);
        return userRepository.save(user);
    }

    private Product createProduct(String productName, double price, User user) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        product.setUser(user);
        return productRepository.save(product);
    }

    @AfterEach
    @Transactional
    void drop() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void testDeletingProductWithAuthorizedUser() throws Exception {
        String token = userAuthService.authentication("user@gmail.com", "Qwerty1!").getToken();
        String requestJson = createDeleteProductRequest(product.getId());

        mockMvc.perform(delete("/rent/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void testDeletingProductWithWrongUser() throws Exception {
        //Create another user to perform the wrong user test
        User otherUser = createUser("otherUser", "otheruser@gmail.com", "USER");

        //Authenticate the otherUser and get the token
        String token = userAuthService.authentication("otheruser@gmail.com", "Qwerty1!").getToken();

        // Create the delete product request JSON
        String requestJson = createDeleteProductRequest(product.getId());

        // Perform the delete request with otherUser's token
        mockMvc.perform(delete("/rent/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You do not have permission to delete this product"));
    }

    @Test
    void testDeletingWithNoHavingPermission() throws Exception {

        User otherUser = createUser("otherUser", "otheruser@gmail.com", "USER");
        String token = userAuthService.authentication("otheruser@gmail.com", "Qwerty1!").getToken();
        String requestJson = createDeleteProductRequest(999L); // Non-existent ID

        mockMvc.perform(delete("/rent/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You do not have permission to delete this product"));
    }

    @Test
    void testDeletingNonExistentProduct() throws Exception {
        // Authenticate as a valid user
        User otherUser = createUser("otherUser", "otheruser@gmail.com", "USER");
        String token = userAuthService.authentication("otheruser@gmail.com", "Qwerty1!").getToken();

        // Use a non-existent product ID (e.g., 999L)
        Long nonExistentProductId = 999L;

        // Attempt to delete the non-existent product
        mockMvc.perform(delete("/rent/" + nonExistentProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound()) // Expect a 404 Not Found status
                .andExpect(jsonPath("$.message").value("Product not found with id: " + nonExistentProductId)); // Validate the response message
    }

    private String createDeleteProductRequest(Long productId) {
        return String.format("{\"id\":%d}", productId);
    }
}