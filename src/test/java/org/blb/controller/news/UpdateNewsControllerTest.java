package org.blb.controller.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.news.NewsDataRequestDto;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.region.Region;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.news.NewsDataRepository;
import org.blb.repository.region.RegionRepository;
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

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UpdateNewsControllerTest {
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
    void testUpdateNewsWithCorrectParams() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();
        NewsDataEntity newsDataEntity = newsDataRepository.findAll().get(0);
        NewsDataRequestDto requestDto = new NewsDataRequestDto(newsDataEntity.getId(), true, false);
        StandardResponseDto responseDto = new StandardResponseDto("Reaction for news with ID = "+newsDataEntity.getId() +" updated successfully");


        mockMvc.perform(put("/news/reaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

    }

    @Test
    void testUpdateNewsWithNotExistingNews() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();
        NewsDataRequestDto requestDto = new NewsDataRequestDto(1000L, true, false);

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("News with ID = 1000 not found"));
    }

    @Test
    void testUpdateNewsWithLikeAndDislike() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        NewsDataEntity newsDataEntity = newsDataRepository.findAll().get(0);

        // Set a reaction with both like and dislike, which should not be allowed
        NewsDataRequestDto requestDto = new NewsDataRequestDto(newsDataEntity.getId(), true, true);
        StandardResponseDto responseDto = new StandardResponseDto("Cannot like and dislike the news simultaneously");

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Cannot like and dislike the news simultaneously"));
    }

    @Test
    void testUpdateNewsWithRemoveReaction() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        NewsDataEntity newsDataEntity = newsDataRepository.findAll().get(0);

        // Set a reaction initially
        NewsDataRequestDto setLikeDto = new NewsDataRequestDto(newsDataEntity.getId(), true, false);
        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setLikeDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Remove the reaction
        NewsDataRequestDto removeReactionDto = new NewsDataRequestDto(newsDataEntity.getId(), false, false);
        StandardResponseDto responseDto = new StandardResponseDto("Reaction for news with ID = " + newsDataEntity.getId() + " updated successfully");

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeReactionDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testUpdateNewsSwitchFromLikeToDislike() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        NewsDataEntity newsDataEntity = newsDataRepository.findAll().get(0);

        // Set a like
        NewsDataRequestDto setLikeDto = new NewsDataRequestDto(newsDataEntity.getId(), true, false);
        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setLikeDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Switch to dislike
        NewsDataRequestDto switchToDislikeDto = new NewsDataRequestDto(newsDataEntity.getId(), false, true);
        StandardResponseDto responseDto = new StandardResponseDto("Reaction for news with ID = " + newsDataEntity.getId() + " updated successfully");

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(switchToDislikeDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testForbiddenIfNotAuthenticated() throws Exception {
        NewsDataEntity newsDataEntity = newsDataRepository.findAll().get(0);
        NewsDataRequestDto requestDto = new NewsDataRequestDto(newsDataEntity.getId(), true, false);

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testValidationForMissingLikedField() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();

        NewsDataRequestDto requestDto = new NewsDataRequestDto(1L, null, false);

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("liked"))
                .andExpect(jsonPath("$.errors[0].message").value("must not be null"));
    }

    @Test
    void testUpdateNewsWithNullParameters() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();

        NewsDataRequestDto requestDto = new NewsDataRequestDto(1L, null, null);

        mockMvc.perform(put("/news/reaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].field", hasItems("liked", "disliked")))
                .andExpect(jsonPath("$.errors[*].message", hasItems("must not be null")));
    }
}