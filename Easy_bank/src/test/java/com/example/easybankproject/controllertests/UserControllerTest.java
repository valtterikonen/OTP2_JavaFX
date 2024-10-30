package com.example.easybankproject.controllertests;

import com.example.easybankproject.controllers.UserController;
import com.example.easybankproject.models.User;
import com.example.easybankproject.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUser_id(1);
        user.setUsername("test1user");
        user.setEmail("test@example.com");
        user.setAddress("123 Test St");
        user.setPhonenumber(123456789);
        user.setFirstname("Test");
        user.setLastname("User");

    }


    @Test
    public void testGetUserDataSuccess() throws Exception {

        String token = "Bearer validToken";
        when(userService.getUserData("validToken")).thenReturn(user);

        mockMvc.perform(get("/api/user/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.address").value("123 Test St"))
                .andExpect(jsonPath("$.phonenumber").value(123456789))
                .andExpect(jsonPath("$.firstname").value("Test"))
                .andExpect(jsonPath("$.lastname").value("User"));
    }

    @Test
    public void testUpdateUserDataSuccess() throws Exception {

        String token = "Bearer validToken";
        when(userService.updateUserData(anyString(), any(User.class))).thenReturn(user);

        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/user/update/me")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.address").value("123 Test St"))
                .andExpect(jsonPath("$.phonenumber").value(123456789))
                .andExpect(jsonPath("$.firstname").value("Test"))
                .andExpect(jsonPath("$.lastname").value("User"));
    }

    @Test
    public void testUpdateUserDataUserNotFound() throws Exception {
        // Arrange
        String token = "Bearer validToken";
        when(userService.updateUserData("validToken", user)).thenReturn(null);

        // Act & Assert
        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/user/update/me")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }
    @Test
    public void testRegisterUserSuccess() throws Exception {
        // Arrange
        when(userService.registerUser(any(User.class))).thenReturn("User registered successfully");

        // Act & Assert
        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }


    @Test
    public void testRegisterUserAlreadyExists() throws Exception {

        when(userService.registerUser(user)).thenReturn("Username already exists.");

        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Username already exists."));
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
        // Arrange
        user.setUsername("testuser");
        user.setPassword("password123");
        when(userService.loginUser(any(User.class))).thenReturn("ValidToken123");

        // Act & Assert
        String jsonRequest = objectMapper.writeValueAsString(user);
        System.out.println(jsonRequest);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string("ValidToken123"));
    }


    @Test
    public void testLoginUserInvalidPassword() throws Exception {

        when(userService.loginUser(user)).thenReturn("Invalid username or password.");

        String jsonRequest = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password."));
    }
}
