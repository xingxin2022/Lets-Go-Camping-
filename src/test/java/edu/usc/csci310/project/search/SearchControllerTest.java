package edu.usc.csci310.project.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkService parkService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void parkController() {
    }

    @Test
    void getParks() throws Exception {
        ParkSearchRequest request = new ParkSearchRequest();
        request.setQuery("Yellow");
        request.setSearchType("parkname");
        request.setStartPosition(0);

        Park park = new Park();
        park.setFullName("Yellow");

        List<Park> parks = Collections.singletonList(park);
        ParkSearchResponse response = new ParkSearchResponse();
        response.setData(parks);

        given(parkService.searchParks(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).willReturn(parks);

        mockMvc.perform(post("/api/search/search-parks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].fullName", is("Yellow")));
    }

    @Test
    void addFavorite() throws Exception {
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUserName("testUser");
        favoriteRequest.setParkCode("yellow");
        favoriteRequest.setParkName("yellow");
        favoriteRequest.setPublic(false);

        FavoriteResponse favoriteResponse = new FavoriteResponse("Park successfully added to favorite list");

        given(parkService.addFavorite(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).willReturn(favoriteResponse);

        mockMvc.perform(post("/api/search/add-favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favoriteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Park successfully added to favorite list")));
    }



    @Test
    void getUserFavorites() throws Exception{
        String username = "testUser";
        List<String> favoriteParkCodes = Collections.singletonList("yellow");

        given(parkService.getFavoriteParkCodes(username)).willReturn(favoriteParkCodes);

        mockMvc.perform(get("/api/search/get-user-favorites")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is("yellow")));
    }


    @Test
    void searchParkById_Success() throws Exception {
        SingleParkSearchRequest request = new SingleParkSearchRequest();
        request.setParkCode("YNP");

        Park park = new Park();
        park.setId("1");
        park.setFullName("Yellowstone National Park");

        List<Park> parks = Collections.singletonList(park);
        given(parkService.fetchParkDetailsBatch(0, Collections.singletonList("YNP"))).willReturn(parks);

        mockMvc.perform(post("/api/search/search-park-by-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is("1")))
                .andExpect(jsonPath("$.data[0].fullName", is("Yellowstone National Park")));
    }

    @Test
    void searchParkById_NotFound() throws Exception {
        SingleParkSearchRequest request = new SingleParkSearchRequest();
        request.setParkCode("INVALID_CODE");

        given(parkService.fetchParkDetailsBatch(0, Collections.singletonList("INVALID_CODE"))).willReturn(Collections.emptyList());

        mockMvc.perform(post("/api/search/search-park-by-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

}