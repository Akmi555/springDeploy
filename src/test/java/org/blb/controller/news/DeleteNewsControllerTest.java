package org.blb.controller.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.exeption.RestException;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.region.Region;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.news.DeleteNewsDataService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class DeleteNewsControllerTest {
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
    private RegionRepository regionRepository;

    @Autowired
    private NewsDataRepository newsDataRepository;

    @MockBean
    private DeleteNewsDataService deleteNewsDataService;

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

        Region region = regionRepository.findById(8L).get();

        NewsDataEntity newsDataEntity = new NewsDataEntity();
        newsDataEntity.setId(2L);
        newsDataEntity.setRegion(region);
        newsDataEntity.setSectionName("inland");
        newsDataEntity.setTitle("Stromausfall in Bad Homburg: Tausende Haushalte und Bahnhof betroffen");
        newsDataEntity.setDate("2024-07-27T12:46:53.250+02:00");
        newsDataEntity.setTitleImageSquare("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlUSc/1x1-840.jpg");
        newsDataEntity.setTitleImageWide("https://images.tagesschau.de/image/622213a8-a3b6-46c1-a5c7-c29c0f9420ad/AAABkPPOA1o/AAABjwnlNY8/16x9-960.jpg");
        newsDataEntity.setContent("<div className=\\\"textValueNews\\\"><strong>In Bad Homburg ist in der Nacht großflächig der Strom ausgefallen. Auch benachbarte Gemeinden waren zum Teil betroffen.</strong></div> ...");
        newsDataEntity.setLikeCount(10);
        newsDataEntity.setDislikeCount(5);
        newsDataEntity.setCommentsCount(1);
        newsDataRepository.save(newsDataEntity);
    }

    @AfterEach
    void drop() {
        newsDataRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testDeleteNews_Success() throws Exception {
        StandardResponseDto responseDto = new StandardResponseDto();
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        StandardDelRequest requestDto = new StandardDelRequest(2L);

        when(deleteNewsDataService.deleteNewsDataById(2L))
                .thenReturn(responseDto);

        mockMvc.perform(delete("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testDeleteNews_NotFound() throws Exception {
        String token = userAuthService.authentication("user2@gmail.com", "Qwerty1!").getToken();

        StandardDelRequest requestDto = new StandardDelRequest(20L);
        when(deleteNewsDataService.deleteNewsDataById(20L))
                .thenThrow(new RestException(HttpStatus.NOT_FOUND, "News with ID = 20 not found"));

        mockMvc.perform(delete("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("News with ID = 20 not found"));

    }
    @Test
    void testDeleteNewsWithWrongUser() throws Exception {
        // Erstelle ein Mock für die Ausnahme
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        StandardDelRequest requestDto = new StandardDelRequest(2L);

        // Simuliere eine SecurityException für den Testfall
        when(deleteNewsDataService.deleteNewsDataById(2L))
                .thenThrow(new RestException(HttpStatus.FORBIDDEN, "User must be registered and has role 'ADMIN' to delete news"));

        // Führe die Anfrage durch und prüfe das Ergebnis
        mockMvc.perform(delete("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteNews_BadRequest_NullId() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        StandardDelRequest requestDto = new StandardDelRequest(null); // Invalid request

        mockMvc.perform(delete("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("id"))
                .andExpect(jsonPath("$.errors[0].message").exists());
    }

    @Test
    void testDeleteNews_BadRequest_InvalidId() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        StandardDelRequest requestDto = new StandardDelRequest(0L); // Invalid request

        mockMvc.perform(delete("/admin/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("id"))
                .andExpect(jsonPath("$.errors[0].message").exists());
    }
}