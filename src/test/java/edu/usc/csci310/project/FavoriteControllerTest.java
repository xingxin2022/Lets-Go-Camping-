package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @Test
    public void movePark_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        doNothing().when(favoriteService).movePark(anyString(), anyString(), anyBoolean());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/movePark")
                        .param("username", "user")
                        .param("parkCode", "park123")
                        .param("moveUp", "true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Park moved successfully."));

        verify(favoriteService, times(1)).movePark("user", "park123", true);
    }

    @Test
    public void removePark_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        doNothing().when(favoriteService).removePark(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/removePark")
                        .param("username", "user")
                        .param("parkCode", "park123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Park removed successfully."));

        verify(favoriteService, times(1)).removePark("user", "park123");
    }

    @Test
    public void updatePrivacy_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        doNothing().when(favoriteService).updateFavoriteListPrivacy(anyString(), anyBoolean());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/updatePrivacy")
                        .param("username", "user")
                        .param("isPublic", "true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Privacy setting updated successfully."));

        verify(favoriteService, times(1)).updateFavoriteListPrivacy("user", true);
    }

    @Test
    public void deleteAll_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        doNothing().when(favoriteService).deleteAll(anyString());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/deleteAll")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("All parks removed successfully."));

        verify(favoriteService, times(1)).deleteAll("user");
    }

    @Test
    public void getFavorites_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        // Arrange
        FavoriteResponse response = new FavoriteResponse(null, "Retrieved successfully", true);
        when(favoriteService.getFavorites(anyString())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/favorites/getFavorites")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Retrieved successfully\",\"success\":true}"));

        verify(favoriteService, times(1)).getFavorites("user");
    }

    @Test
    public void handleException_ShouldReturnInternalServerError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Unexpected error")).when(favoriteService)
                                        .movePark(anyString(), anyString(), anyBoolean());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/movePark")
                        .param("username", "user")
                        .param("parkCode", "park123")
                        .param("moveUp", "true")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }

    @Test
    public void removePark_ShouldHandleException() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(favoriteService).removePark(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/removePark")
                        .param("username", "user")
                        .param("parkCode", "park123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));
    }

    @Test
    public void updatePrivacy_ShouldHandleException() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Privacy update failed")).when(favoriteService).updateFavoriteListPrivacy(anyString(), anyBoolean());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/updatePrivacy")
                        .param("username", "user")
                        .param("isPublic", "false")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Privacy update failed"));
    }

    @Test
    public void deleteAll_ShouldHandleException() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Failed to delete all parks")).when(favoriteService).deleteAll(anyString());

        // Act & Assert
        mockMvc.perform(post("/api/favorites/deleteAll")
                        .param("username", "user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to delete all parks"));
    }

    @Test
    public void getFavorites_ShouldHandleException() throws Exception {
        // Assuming that the username parameter is needed
        String username = "testuser";

        // Mock the behavior of the service to throw an exception
        when(favoriteService.getFavorites(anyString())).thenThrow(new RuntimeException("Failed to fetch favorites"));

        // Perform the request and assert the response structure and content
        mockMvc.perform(post("/api/favorites/getFavorites")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to fetch favorites"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.parks").isEmpty());
    }

    @Test
    void whenFetchDetailsSuccess_thenReturnsStatus200() throws Exception {
        ParkCodesRequest request = new ParkCodesRequest();
        request.setParkCodes(Arrays.asList("zion", "yose"));
        Park p1 = new Park();
        p1.setParkCode("zion");
        p1.setFullName("Zion National Park");
        Park p2 = new Park();
        p2.setParkCode("yose");
        p2.setFullName("Yosemite National Park");
        List<Park> parks = Arrays.asList(p1, p2);
        FavoriteResponse response = new FavoriteResponse(parks, null, true);

        given(favoriteService.fetchParkDetailsBatch(0, request.getParkCodes())).willReturn(parks);

        mockMvc.perform(post("/api/favorites/fetchDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void whenFetchDetailsFails_thenReturnsStatus500() throws Exception {
        ParkCodesRequest request = new ParkCodesRequest();
        request.setParkCodes(Arrays.asList("zion", "yose"));

        given(favoriteService.fetchParkDetailsBatch(0, request.getParkCodes())).willThrow(new RuntimeException("Service failed"));

        mockMvc.perform(post("/api/favorites/fetchDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Service failed"));
    }
}
