package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void loginUserTest() throws Exception {
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
        assertNotNull(session, "Session should not be null");
        assertEquals(username, session.getAttribute("username"), "Session attribute 'username' should match the provided username");
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
}
