package edu.usc.csci310.project;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void registerUser_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        String username = "user";
        String password = "Password123";
        String confirmPassword = "password";
        when(userService.registerUser(any(User.class), eq(confirmPassword)))
                .thenReturn(new RegisterResponse("User registered successfully"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .param("username", username)
                        .param("password", password)
                        .param("confirmPassword", confirmPassword)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully")));

        verify(userService, times(1)).registerUser(any(User.class), eq(confirmPassword));
    }

    @Test
    public void loginUser_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        String username = "user";
        String password = "Password123";
        when(userService.loginUser(username, password))
                .thenReturn(new LoginResponse("Login Successful"));

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login Successful")));

        verify(userService, times(1)).loginUser(username, password);
    }

    @Test
    public void logoutUserTest() throws Exception {
        mockMvc.perform(post("/api/users/logout")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out successfully")));
    }

    @Test
    public void GetCurrentUserTest() throws Exception {
        String username = "user";
        mockMvc.perform(get("/api/users/current-user")
                        .sessionAttr("username", username))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)));
    }

    @Test
    public void getNoCurrentUserTest() throws Exception {
        mockMvc.perform(get("/api/users/current-user"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("User not logged in")));
    }

    

    @Test
    public void handleException_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        when(userService.registerUser(any(User.class), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .param("username", "user")
                        .param("password", "Password123")
                        .param("confirmPassword", "Password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("An unexpected error occurred: Unexpected error")));
    }
}
