package edu.usc.csci310.project.search;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkService parkService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetParks() throws Exception {
        ParkSearchRequest request = new ParkSearchRequest();
        request.setQuery("Yosemite");
        request.setSearchType("National Park");
        request.setStartPosition(0);

        Park park = new Park();
        park.setId("1");
        park.setFullName("Yosemite National Park");

        when(parkService.searchParks(anyString(), anyString(), anyInt()))
                .thenReturn(Arrays.asList(park));

//        mockMvc.perform(post("/search-parks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].id").value(""))
//                .andExpect(jsonPath("$.data[0].fullName").value("Yosemite National Park"));

//        verify(parkService).searchParks("Yosemite", "National Park", 0);
    }
}
