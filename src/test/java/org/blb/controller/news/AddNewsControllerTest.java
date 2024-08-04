package org.blb.controller.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.exeption.RestException;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.news.AddNewsDataService;
import org.blb.service.user.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class AddNewsControllerTest {
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
    private NewsDataRepository newsDataRepository;

    @MockBean
    private AddNewsDataService addNewsDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        Role role = roleRepository.findByRole("USER");
        testUser.setName("testUser");
        testUser.setEmail("user1@gmail.com");
        testUser.setPassword(passwordEncoder.encode("Qwerty1!"));
        testUser.setRole(role);
        testUser.setCode("");
        testUser.setState(State.CONFIRMED);
        userRepository.save(testUser);

        User test2User = new User();
        test2User.setName("testUser2");
        test2User.setEmail("user2@gmail.com");
        test2User.setPassword(passwordEncoder.encode("Qwerty1!"));
        role = roleRepository.findByRole("ADMIN");
        test2User.setRole(role);
        test2User.setCode("");
        test2User.setState(State.CONFIRMED);
        userRepository.save(test2User);

    }
    @AfterEach
    void drop() {
        newsDataRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_success() throws Exception {
        StandardResponseDto responseDto = new StandardResponseDto();
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenReturn(responseDto);

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testAddNewsWithAuthorizedUserButNotAdmin() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.FORBIDDEN, "User must be registered and has role 'ADMIN' to delete news"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_NoContent() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.NO_CONTENT, "Fetch response contains no data"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("Fetch response contains no data"));
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_JSONProcessingError() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing JSON response: error details here"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error processing JSON response: error details here"));
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_DetailsURLJSONProcessingError() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing details URL JSON: error details here"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error processing details URL JSON: error details here"));
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_FetchingDataFromDetailsURLError() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from details URL: http://example.com/details"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error fetching data from details URL: http://example.com/details"));
    }

    @Test
    void testAddNewsWithAuthorizedAdmin_FetchingDataFromURLError() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        when(addNewsDataService.saveNewsFromFetchApi())
                .thenThrow(new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching data from URL: http://example.com"));

        mockMvc.perform(post("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error fetching data from URL: http://example.com"));
    }
}