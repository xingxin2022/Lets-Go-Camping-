package edu.usc.csci310.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        MvcResult result = mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login Successful")))
                .andReturn();

        verify(userService, times(1)).loginUser(username, password);
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session);
        assertEquals(session.getAttribute("username"), username);
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

    @Test
    public void noLoginUserTest() throws Exception {
        // Arrange
        String username = "user";
        String password = "wrongpassword";
        when(userService.loginUser(username, password))
                .thenReturn(new LoginResponse("Login Failed"));

        // Act
        MvcResult result = mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login Failed")))
                .andReturn();

        // Assert
        verify(userService, times(1)).loginUser(username, password);

        // Verify session attribute is not set due to failed login
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertTrue(session == null || session.getAttribute("username") == null, "Session attribute 'username' should not be set on failed login"); // Assert that the session attribute "username" is not set
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
    public void logoutUserTest() throws Exception {
        mockMvc.perform(post("/api/users/logout")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out successfully")));
    }

    @Test
    public void getAllUsernames_ShouldReturnListOfUsernames() throws Exception {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2", "user3");
        when(userService.getAllUsernames()).thenReturn(usernames);

        // Act & Assert
        mockMvc.perform(get("/api/users/get-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is("user1")))
                .andExpect(jsonPath("$[1]", is("user2")))
                .andExpect(jsonPath("$[2]", is("user3")));

        verify(userService, times(1)).getAllUsernames();
    }

    @Test
    public void getUserFavoritePark_ShouldReturnListOfParks() throws Exception {
        String username = "user1";
        List<ParkInfo> parkInfos = new ArrayList<>();
        parkInfos.add(new ParkInfo("Yellowstone", "YNP", false));
        when(userService.getFavoriteParksByUsername(username)).thenReturn(parkInfos);

        mockMvc.perform(get("/api/users/{username}/parks", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].parkName", is("Yellowstone")))
                .andExpect(jsonPath("$[0].parkCode", is("YNP")));
    }


    @Test
    public void getUnionOfFavoriteParks_ShouldReturnListOfParkCounts() throws Exception {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2");
        List<ParkCount> parkCounts = new ArrayList<>();
        parkCounts.add(new ParkCount("Yellowstone", "YNP"));
        when(userService.getUnionFavoriteParks(usernames)).thenReturn(parkCounts);

        // Act & Assert
        mockMvc.perform(post("/api/users/favorite-parks/union")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].parkName", is("Yellowstone")))
                .andExpect(jsonPath("$[0].parkCode", is("YNP")));

        verify(userService, times(1)).getUnionFavoriteParks(usernames);
    }

    @Test
    public void getUnionOfFavoriteParks_ShouldReturnInternalServerError_WhenRuntimeException() throws Exception {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2");
        when(userService.getUnionFavoriteParks(usernames)).thenThrow(new RuntimeException("Internal server error occurred"));

        // Act & Assert
        mockMvc.perform(post("/api/users/favorite-parks/union")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usernames)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Internal server error occurred")));

        verify(userService, times(1)).getUnionFavoriteParks(usernames);
    }

    @Test
    public void getUnionOfFavoriteParks_ShouldReturnBadRequest_WhenException() throws Exception {
        // Arrange
        List<String> usernames = Arrays.asList("user1", "user2");
        when(userService.getUnionFavoriteParks(usernames)).thenThrow(new Exception("Bad request due to invalid input"));

        // Act & Assert
        mockMvc.perform(post("/api/users/favorite-parks/union")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usernames)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Bad request due to invalid input")));

        verify(userService, times(1)).getUnionFavoriteParks(usernames);
    }



}

