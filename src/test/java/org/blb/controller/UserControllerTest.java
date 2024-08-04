package org.blb.controller;

import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthService userAuthService;


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
        userRepository.deleteAll();
    }

    @Test
    void testUserAuthWith() throws Exception {
        String auth = """
                {
                "email":"user2@gmail.com",
                    "password":"Qwerty1!"}
                """;
        mockMvc.perform(post("/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));

    }

    @Test
    void testUserRegister() throws Exception {
        String requestJson = """
                {
                "name":"newuser",
                "email":"user3@test.com",
                    "password":"Qwerty1!"}
                """;
        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Confirmation sanded to your email"));

    }

    @Test
    void testUserRegisterWhenUserEmailAlreadyExist() throws Exception {
        String requestJson = """
                {
                "name":"newuser",
                "email":"user2@gmail.com",
                    "password":"Qwerty1!"}
                """;
        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testGettingUserListWithAdminRole() throws Exception {
        String token = userAuthService.authentication("user2@gmail.com", "Qwerty1!").getToken();
           mockMvc.perform(get("/admin/user/0")
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testGettingUserListWithUserRole() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        mockMvc.perform(get("/admin/user/0")
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatingUserStateWithUserRole() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        User user = userRepository.findUserByEmail("user1@gmail.com").get();
        String requestJson = """
                {
                "id":
                """+
                user.getId()
                +"""
                 ,
                "confirmed":true}
                """;
        mockMvc.perform(put("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatingUserStateWithAdminRole() throws Exception {
        String token = userAuthService.authentication("user2@gmail.com", "Qwerty1!").getToken();
        User user = userRepository.findUserByEmail("user1@gmail.com").get();
        String requestJson = """
                {
                "id":
                """+
                user.getId()
                +"""
                 ,
                "confirmed":true}
                """;
        mockMvc.perform(put("/admin/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
    @Test
    void testUpdatingUseRoleWithUserRole() throws Exception {
        String token = userAuthService.authentication("user1@gmail.com", "Qwerty1!").getToken();
        User user = userRepository.findUserByEmail("user1@gmail.com").get();
        Role role = roleRepository.findByRole("ADMIN");
        String requestJson = """
                {
                "userId":
                """+
                user.getId()
                +"""
                 ,
                "roleId":
                """+
                role.getId()
                +"}";
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdatingUserRoleWithAdminRole() throws Exception {
        String token = userAuthService.authentication("user2@gmail.com", "Qwerty1!").getToken();
        User user = userRepository.findUserByEmail("user2@gmail.com").get();
        Role role = roleRepository.findByRole("ADMIN");
        String requestJson = """
                {
                "userId":
                """+
                user.getId()
                +"""
                 ,
                "roleId":
                """+
                role.getId()
                +"}";
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}
