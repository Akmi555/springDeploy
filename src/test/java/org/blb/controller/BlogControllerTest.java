package org.blb.controller;

import jakarta.transaction.Transactional;
import org.blb.models.blog.Blog;
import org.blb.models.blog.BlogComment;
import org.blb.models.region.Region;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogCommentRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.blog.BlogDataService;
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
public class BlogControllerTest {
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
    private BlogRepository blogRepository;
    @Autowired
    private BlogCommentRepository blogCommentRepository;

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
        Region region = regionRepository.findById(3L).get();
        Blog blog = new Blog("Some Title", "Some content", test2User, 0,0,region);
        blogRepository.save(blog);
        BlogComment blogComment = new BlogComment("Some comment", test2User, blog);
        blogCommentRepository.save(blogComment);
    }

    @AfterEach
    @Transactional
    void drop() {
        userRepository.deleteAll();blogRepository.deleteAll();
    }

    @Test
    void testGettingBlogsWithCorrectRegion() throws Exception {
        mockMvc.perform(get("/blogs?page=0&region=2"))
                .andExpect(status().isOk());
    }

    @Test
    void testGettingBlogsOfUser() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        mockMvc.perform(get("/blogs/user?page=0")
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testGettingBlogsWithWrongRegion() throws Exception {
        mockMvc.perform(get("/blogs?page=0&region=70"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBlogAddingWithAuthorizedUser() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        String requestJson = """
                {
                "title": "Some title",
                "content": "Some content",
                "region": 7}
                """;
        mockMvc.perform(post("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    void testBlogAddingWithAuthorizedUserWrongRegionId() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        String requestJson = """
                {
                "title": "Some title",
                "content": "Some content",
                "region": 77}
                """;
        mockMvc.perform(post("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
    @Test
    void testBlogDeletingWithCorrectParams() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
         String requestJson = """
                {
                "id":
                """+blog.getId()+"}";
        mockMvc.perform(delete("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void testBlogDeletingWithWrongUser() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        String requestJson = """
                {
                "id":
                """+blog.getId()+"}";
        mockMvc.perform(delete("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You dont have permission to update this blog"));
    }

    @Test
    void testBlogDeletingWithNonExistBlog() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        Long id = 1L;
        if(id.equals(blog.getId())){
            id++;
        }
        String requestJson = """
                {
                "id":
                """+id+"}";
        mockMvc.perform(delete("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found"));
    }

    @Test
    void testBlogUpdatingWithCorrectParams() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        String requestJson = """
                {
                "blogAddRequestDTO":{
                "title":"New Some title",
                "content": "New Some content",
                "region": 7},
                "id":
                """+blog.getId()+"}";
        mockMvc.perform(put("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void testBlogUpdatingWithWrongUser() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        String requestJson = """
                {
                "blogAddRequestDTO":{
                "title":"New Some title",
                "content": "New Some content",
                "region": 7},
                "id":
                """+blog.getId()+"}";
        mockMvc.perform(put("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You dont have permission to update this blog"));
    }

    @Test
    void testBlogUpdatingWithNonExistBlog() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        String requestJson = """
                {
                "blogAddRequestDTO":{
                "title":"New Some title",
                "content": "New Some content",
                "region": 7},
                "id":0}
                """;
        mockMvc.perform(put("/blog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found"));
    }
    @Test
    void testBlogCommentAddingWithAuthorizedUser() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        Blog blog = blogRepository.findAll().get(0);
        String requestJson = """
                {
                  "comment": "some comment",
                  "blogId":
                  """+
                blog.getId()+
                "}";
        mockMvc.perform(post("/blog/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Comment added successfully"));

    }
    @Test
    void testBlogCommentAddingWithNonExistComment() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        String requestJson = """
                {
                  "comment": "some comment",
                  "blogId":0
                }
                """;
        mockMvc.perform(post("/blog/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Blog not found"));

    }
    @Test
    void testBlogCommentDeletingWithAuthorizedUser() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        BlogComment blogComment = blogCommentRepository.findAll().get(0);
        String requestJson = """
                {
                 "id":
                """+
                blogComment.getId()+
                "}";
        mockMvc.perform(delete("/blog/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Comment deleted successfully"));

    }
    @Test
    void testBlogCommentDeletingWithWrongUser() throws Exception {
        String token = userAuthService.
                authentication("user1@gmail.com", "Qwerty1!").getToken();
        BlogComment blogComment = blogCommentRepository.findAll().get(0);
        String requestJson = """
                {
                 "id":
                """+
                blogComment.getId()+
                "}";
        mockMvc.perform(delete("/blog/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("You dont have permission to dell this message"));

    }
    @Test
    void testBlogCommentDeletingWithNonExistComment() throws Exception {
        String token = userAuthService.
                authentication("user2@gmail.com", "Qwerty1!").getToken();
        BlogComment blogComment = blogCommentRepository.findAll().get(0);
        Long id=1L;
        if(id.equals(blogComment.getId())){
            id++;
        }
       String requestJson = """
                {
                 "id":
                """
                +id+"}";
        mockMvc.perform(delete("/blog/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"));

    }
}
