package edu.usc.csci310.project;

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
    public void registerUser_ShouldReturnConflict_WhenUserAlreadyExists() throws Exception {
        // Arrange
        when(userService.registerUser(any(User.class), anyString()))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .param("username", "existingUser")
                        .param("password", "Password123")
                        .param("confirmPassword", "Password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("User already exists")));
    }

    @Test
    public void registerUser_ShouldReturnBadRequest_WhenInvalidPassword() throws Exception {
        // Arrange
        when(userService.registerUser(any(User.class), anyString()))
                .thenThrow(new InvalidPasswordException("Invalid password"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .param("username", "user")
                        .param("password", "pass")
                        .param("confirmPassword", "pass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid password")));
    }

    @Test
    public void loginUser_ShouldReturnUnauthorized_WhenLoginFailed() throws Exception {
        // Arrange
        when(userService.loginUser(anyString(), anyString()))
                .thenThrow(new LoginFailedException("Login failed"));

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                        .param("username", "user")
                        .param("password", "wrongPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Login failed")));
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

