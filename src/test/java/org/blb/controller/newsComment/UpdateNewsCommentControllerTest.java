package org.blb.controller.newsComment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.newsComment.UpdateCommentRequestDTO;
import org.blb.models.news.NewsComment;
import org.blb.models.news.NewsDataEntity;
import org.blb.models.region.Region;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.news.NewsCommentRepository;
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

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UpdateNewsCommentControllerTest {
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

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private NewsCommentRepository newsCommentRepository;

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
        testUser.setCode("USER_CODE_1");
        testUser.setState(State.CONFIRMED);
        userRepository.save(testUser);

        User testUser2 = new User();
        role = roleRepository.findByRole("USER");
        testUser2.setName("testUser2");
        testUser2.setEmail("user2@gmail.com");
        testUser2.setPassword(passwordEncoder.encode("Qwerty1!"));
        testUser2.setRole(role);
        testUser2.setCode("USER_CODE_2");
        testUser2.setState(State.CONFIRMED);
        userRepository.save(testUser2);

        User testUserAdmin = new User();
        testUserAdmin.setName("testUserAdmin");
        testUserAdmin.setEmail("testUserAdmin@gmail.com");
        testUserAdmin.setPassword(passwordEncoder.encode("Qwerty1!"));
        role = roleRepository.findByRole("ADMIN");
        testUserAdmin.setRole(role);
        testUserAdmin.setCode("ADMIN_CODE");
        testUserAdmin.setState(State.CONFIRMED);
        userRepository.save(testUserAdmin);

        Region region = regionRepository.findById(8L).get();

        NewsDataEntity newsDataEntity = new NewsDataEntity();
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

        NewsDataEntity newsDataEntityDB = newsDataRepository.findAll().get(0);
        User user1 = userRepository.findUserByEmail("user1@gmail.com").orElseThrow();

        NewsComment newsComment = new NewsComment();
        newsComment.setComment("Test comment message");
        newsComment.setUser(user1);
        newsComment.setNewsDataEntity(newsDataEntityDB);
        newsComment.setCommentDate(LocalDateTime.parse("2024-07-26T13:30:00"));
        newsCommentRepository.save(newsComment);

    }
    @AfterEach
    void drop() {
        newsDataRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testUpdateNewsComment_Success() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();

        UpdateCommentRequestDTO requestDto = new UpdateCommentRequestDTO(newsCommentRepository.findAll().get(0).getId(), "UPDATED COMMENT");

        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("Comment with ID = "+ requestDto.getId() +" updated successfully");

        mockMvc.perform(put("/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testUpdateNewsCommentWithNotExistingId() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();

        UpdateCommentRequestDTO requestDto = new UpdateCommentRequestDTO(11111111L, "UPDATED COMMENT");

        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("Comment with ID = "+ requestDto.getId() +" not found");

        mockMvc.perform(put("/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testUpdateNewsCommentWithoutAuth() throws Exception {
        UpdateCommentRequestDTO requestDto = new UpdateCommentRequestDTO(newsCommentRepository.findAll().get(0).getId(), "UPDATED COMMENT");

        mockMvc.perform(put("/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateNewsCommentByAnotherUser() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();

        UpdateCommentRequestDTO requestDto = new UpdateCommentRequestDTO(newsCommentRepository.findAll().get(0).getId(), "UPDATED COMMENT");

        StandardResponseDto responseDto = new StandardResponseDto();
        responseDto.setMessage("You don't have permission to update this comment");

        mockMvc.perform(put("/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testUpdateNewsCommentWithNullIdAndNullContent() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();

        UpdateCommentRequestDTO requestDto = new UpdateCommentRequestDTO(null, null);

        String expectedJson = "{ \"errors\": [ { \"field\": \"comment\", \"message\": \"must not be blank\" }, { \"field\": \"id\", \"message\": \"must not be null\" } ] }";

        mockMvc.perform(put("/news/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson));
    }
}