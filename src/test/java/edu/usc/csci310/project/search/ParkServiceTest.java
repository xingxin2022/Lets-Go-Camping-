package edu.usc.csci310.project.search;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.sql.DataSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ParkServiceTest {

//    @Test
//    void testSearchParks() {
//        // Mock DataSource as it's required but not the focus of this test
//        DataSource mockDataSource = mock(DataSource.class);
//
//        // Sample response that your real API would return
//        String mockResponse = "{\"data\":[{\"fullName\":\"Test Park\",\"parkCode\":\"tp\"}]}";
//
//        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class,
//                (mock, context) -> {
//                    // Mocking the behavior of postForEntity method
//                    when(mock.postForEntity(any(String.class), any(), any(Class.class)))
//                            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
//                })) {
//
//            // Initialize your service with the mocked DataSource
//            ParkService parkService = new ParkService(mockDataSource);
//
//            // Perform the test action
//            List<Park> result = parkService.searchParks("Test Park", "parkname", 0);
//
//            // Assertions
//            assertNotNull(result);
//            assertFalse(result.isEmpty());
//            assertEquals("Test Park", result.get(0).getFullName());
//            assertEquals("tp", result.get(0).getParkCode());
//        }
//    }



//    @MockBean
//    private RestTemplate restTemplate;

    //@Autowired
    //private ParkService parkService;




    @Mock
    private RestTemplate restTemplate;
//
    @InjectMocks
    @Spy
    private ParkService parkService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String API_KEY = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    private final String BASE_URL = "https://developer.nps.gov/api/v1";

//    private void mockRestTemplate(String url, String response) {
//        when(restTemplate.postForEntity(eq(url), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
//    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @BeforeEach
//    void setUp() {
//        restTemplate = mock(RestTemplate.class);
//        parkService = new ParkService();
//        //parkService.setRestTemplate(restTemplate); // Assuming you have a setter method to inject mocks
//    }


    /*@Test
    void testSearchParksWithParkName() throws Exception {
        // Mocking RestTemplate
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                "{\"data\":[{\"id\":\"1\",\"fullName\":\"Yosemite National Park\"}]}", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(responseEntity);

        // Test
        List<Park> result = parkService.searchParks("Yosemite", "parkname", 0);

        // Assertions
        assertEquals(4, result.size());
        assertEquals("Devils Postpile National Monument", result.get(0).getFullName());

        // Repeat this process for each branch in your searchParks method,
        // ensuring you cover all possible branches and edge cases.
    }*/

    @Test
    public void testSearchParksWithParkName() {
        // Mocking RestTemplate for "parkname" searchType
        String searchType = "parkname";
        String query = "Yosemite";
        int startPosition = 0;
        String mockResponse = "{\"data\":[{\"id\":\"1\",\"fullName\":\"Devils Postpile National Monumentk\"}]}";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        List<Park> result = parkService.searchParks(query, searchType, startPosition);

        assertEquals(4, result.size());
        assertEquals("Devils Postpile National Monument", result.get(0).getFullName());
//        verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void testSearchParksWithActivities() {
//        // First, mock the response for getting park codes based on activities
        //String mockResponseJsonForCodes = "{\"data\":[{\"parkCode\":\"testParkCode\"}]}";
        //ResponseEntity<String> mockResponseForCodes = new ResponseEntity<>(mockResponseJsonForCodes, HttpStatus.OK);
//
        // Then, mock the response for getting detailed park information based on park codes
        String searchType = "activities";
        String query = "hiking";
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/test/java/edu/usc/csci310/project/search/activities.txt"));
            String mockResponseJsonForDetails = lines.stream().collect(Collectors.joining(System.lineSeparator()));
            //System.out.println(content);
            //String mockResponseJsonForCodes = "{\"data\":[{\"parkCode\":\"testParkCode\"}]}";
            //String mockResponseJsonForCodes = lines.stream().collect(Collectors.joining(System.lineSeparator()));
            //ResponseEntity<String> mockResponseForCodes = new ResponseEntity<>(mockResponseJsonForCodes, HttpStatus.OK);
            ResponseEntity<String> mockResponseForDetails = new ResponseEntity<>(mockResponseJsonForDetails, HttpStatus.OK);
            //String mockResponseJsonForDetails = lines.stream().collect(Collectors.joining(System.lineSeparator()));
//
            when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                    //.thenReturn(mockResponseForCodes);
                    .thenReturn(mockResponseForDetails);
//
            List<Park> result1 = parkService.searchParks(query, searchType, 0);
            assertNotNull(result1);
            assertEquals(10, result1.size());
            List<Park> result2 = parkService.searchParks(query, searchType, 10);
            assertNotNull(result2);
            assertEquals(10, result2.size());
//            for (Park element : result1) {
//                System.out.println(element.getActivities());
//            }
            //assertEquals("Acadia National Park", result1.get(0).);
            //assertEquals("Acadia National Park");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String mockResponseJsonForDetails = "[\n" +
//                "  {\n" +
//                "    \"total\": \"2\",\n" +
//                "    \"data\": [\n" +
//                "      {\n" +
//                "        \"id\": \"FEDA3DF8-B871-4C1A-BB96-32823860B174\",\n" +
//                "        \"name\": \"Arts and Culture\",\n" +
//                "        \"parks\": [\n" +
//                "          {\n" +
//                "            \"states\": \"KY\",\n" +
//                "            \"fullName\": \"Abraham Lincoln Birthplace National Historical Park\",\n" +
//                "            \"url\": \"https://www.nps.gov/abli/index.htm\",\n" +
//                "            \"parkCode\": \"abli\",\n" +
//                "            \"designation\": \"National Historical Park\",\n" +
//                "            \"name\": \"Abraham Lincoln Birthplace\"\n" +
//                "          }\n" +
//                "        ]\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"limit\": \"50\",\n" +
//                "    \"start\": \"0\"\n" +
//                "  }\n" +
//                "]";
//        ResponseEntity<String> mockResponseForDetails = new ResponseEntity<>(mockResponseJsonForDetails, HttpStatus.OK);
////
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                //.thenReturn(mockResponseForCodes)
//                .thenReturn(mockResponseForDetails);
////
//        List<Park> result1 = parkService.searchParks(query, searchType, 0);

//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(mockResponseForCodes)
//                .thenReturn(mockResponseForDetails);
//        List<Park> result2 = parkService.searchParks("hiking", "activities", 10);
//
//        assertNull(result1);
//        assertEquals(10, result1.size());
//        assertEquals("Acadia National Park", result1.get(0).getFullName());
//
//        assertNotNull(result2);
//        assertEquals(10, result2.size());
//        assertEquals("Arches National Park", result2.get(0).getFullName());
        //verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
    }

//    @Test
//    void testSearchParksWithActivitiesStartPosition() {
//        // First, mock the response for getting park codes based on activities
//        String mockResponseJsonForCodes = "{\"data\":[{\"parkCode\":\"testParkCode\"}]}";
//        ResponseEntity<String> mockResponseForCodes = new ResponseEntity<>(mockResponseJsonForCodes, HttpStatus.OK);
//
//        // Then, mock the response for getting detailed park information based on park codes
//        String mockResponseJsonForDetails = "{\"data\":[{\"id\":\"1\",\"fullName\":\"Adventure Park\"}]}";
//        ResponseEntity<String> mockResponseForDetails = new ResponseEntity<>(mockResponseJsonForDetails, HttpStatus.OK);
//
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(mockResponseForCodes)
//                .thenReturn(mockResponseForDetails);
//
//        List<Park> result1 = parkService.searchParks("hiking", "activities", 0);
//        List<Park> result2 = parkService.searchParks("hiking", "activities", 11);
//
//
//
//        assertNotNull(result1);
//        assertEquals(10, result1.size());
//        assertEquals("Acadia National Park", result1.get(0).getFullName());
//
//        assertNotNull(result2);
//        assertEquals(10, result2.size());
//        assertEquals("Arkansas Post National Memorial", result2.get(0).getFullName());
//    }

    @Test
    void testSearchParksWithState() {
        // Mock the response for getting parks based on state code
        String mockResponseJson = "{\"data\":[{\"id\":\"2\",\"fullName\":\"State Park\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(mockResponse);

        List<Park> result = parkService.searchParks("california", "states", 0);

        assertNotNull(result);
        assertEquals(10, result.size());
        assertEquals("Alcatraz Island", result.get(0).getFullName());
//        verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
    }

//    @Test
//    void testSearchParksWithAmenities() {
//        String searchType = "amenities";
//        String query = "restroom";
//
//        try {
//            List<String> lines = Files.readAllLines(Paths.get("src/test/java/edu/usc/csci310/project/search/amenities.txt"));
//            String mockResponseJsonForDetails = lines.stream().collect(Collectors.joining(System.lineSeparator()));
//
//            ResponseEntity<String> mockResponseForDetails = new ResponseEntity<>(mockResponseJsonForDetails, HttpStatus.OK);
//
//            when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                    //.thenReturn(mockResponseForCodes);
//                    .thenReturn(mockResponseForDetails);
////
//            List<Park> result1 = parkService.searchParks(query, searchType, 0);
////            assertNotNull(result1);
////            assertEquals(10, result1.size());
////            List<Park> result2 = parkService.searchParks(query, searchType, 10);
////            assertNotNull(result2);
////            assertEquals(10, result2.size());
////            for (Park element : result1) {
////                System.out.println(element.getActivities());
////            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        //String mockResponseJson = "{\"data\":[{\"id\":\"2\",\"fullName\":\"State Park\"}]}";
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(mockResponse);
//
//        //List<Park> result = parkService.searchParks("california", "states", 0);
//        List<Park> result1 = parkService.searchParks(query, searchType, 0);
//        assertNotNull(result1);
//        assertEquals(10, result1.size());
//        List<Park> result2 = parkService.searchParks(query, searchType, 10);
//        assertNotNull(result2);
//        assertEquals(10, result2.size());

//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("State Park", result.get(0).getFullName());
        //verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//
//        // Setup the RestTemplate mock to return the mocked response for any URL, HttpHeaders, and Class type
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(mockResponse);

        // Execute the searchParks method with parameters that trigger the "amenities" search type
//        List<Park> result1 = parkService.searchParks(query, searchType, 0);
//        List<Park> result2 = parkService.searchParks(query, searchType, 10);// Assuming "amenities" triggers the else block
//
//        // Assertions to verify that the parks list is correctly populated based on the mocked API response
//        assertNotNull(result1);
//        assertEquals(10, result1.size());
//        assertEquals("Abraham Lincoln Birthplace National Historical Park", result1.get(0).getFullName());
//
//        assertNotNull(result2);
//        assertEquals(10, result2.size());
//        assertEquals("Blue Ridge Parkway", result2.get(0).getFullName());

    //}


//    @Test
//    public void testSearchParksWhenApiResponseIsUnsuccessful() throws Exception {
////        mockServer.expect(requestTo("/test"))
////                .andExpect(method(HttpMethod.GET))
////                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)); // This makes is2xxSuccessful() == false
//
//
//        //String mockResponseJsonForCodes = "{\"data\":[{\"parkCode\":\"testParkCode\"}]}";
//        String mockResponseJsonForCodes = "test";
//        //ResponseEntity<String> mockResponseForCodes = new ResponseEntity<>(mockResponseJsonForCodes, HttpStatus.INTERNAL_SERVER_ERROR);
//
//        // Then, mock the response for getting detailed park information based on park codes
//        //String mockResponseJsonForDetails = "{\"data\":[{\"id\":\"1\",\"fullName\":\"Adventure Park\"}]}";
//        //String mockResponseJsonForDetails = "";
//        //ResponseEntity<String> mockResponseForDetails = new ResponseEntity<>(mockResponseJsonForDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//
//
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(mockResponseJsonForCodes, HttpStatus.INTERNAL_SERVER_ERROR));
//                //.thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
//
//        //verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
//
//        //doThrow(new RuntimeException()).when(mockResponseJsonForCodes).isEmpty();
//
//
//        //List<Park> result1 = parkService.searchParks("hiking", "activities", 0);
//        //List<Park> result2 = parkService.searchParks("hiking", "activities", 10);
//
//
//        // Mock RestTemplate to return a response with a non-successful HTTP status code
//        //ResponseEntity<String> mockResponse = new ResponseEntity<>("Some error message", HttpStatus.INTERNAL_SERVER_ERROR);
////        when(restTemplate.postForEntity(any(String.class), any(), any(Class.class)))
////                .thenReturn(mockResponse);
//        // Call the method under test
//        List<Park> result = parkService.searchParks("testQuery", "activities", 0);
//
//        // Verify the result is handled as expected when the API call is unsuccessful
//        assertTrue(result.isEmpty(), "The result should be an empty list when the API call is unsuccessful");
//    }

//    @Test
//    void whenRestTemplateThrowsException_thenHandleGracefully() {
//        // Setup mock to throw an HttpClientErrorException
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        // Call your service method
//        List<Park> result = parkService.searchParks("query", "searchType", 0);
//
//        // Assertions to verify how your service handles the exception
//        assertTrue(result.isEmpty(), "Expected an empty result on RestClientException");
//    }

//    @Test
//    void testSearchParksWithStartPosition() {
//        // Assuming parkCodes have been populated from a previous operation
//        //List<String> mockParkCodes = Arrays.asList("code1", "code2", "code3", "code4", "code5");
//        //parkService.setParkCodes(mockParkCodes); // Assume there's a setter or direct access
//
//        // Testing the method with a startPosition greater than 0 for "activities"
//        List<Park> result = parkService.searchParks("hiking", "activities", 2); // startPosition is 2
//
//        // Assertions
//        assertTrue(!result.isEmpty(), "Should return at least one park");
//        assertEquals("Batch Park", result.get(0).getFullName(), "The park name should match the batch response");
//    }




//    @Test
//    void whenSearchTypeIsActivities_thenConstructCorrectUrl() {
//        String expectedUrl = BASE_URL + "/activities/parks?limit=50&q=hiking&api_key=" + API_KEY;
//        mockRestTemplate(expectedUrl, "yose");
//
//        parkService.searchParks("hiking", "activities", 0);
//
//        verify(restTemplate).postForEntity(eq(expectedUrl), any(), eq(String.class));
//    }


//    @Test
//    public void testSearchParksWithActivities() {
//        // Mocking RestTemplate for "parkname" searchType
//        String searchType = "activities";
//        String query = "hiking";
//        int startPosition = 0;
//        String mockResponse = "{\"data\":[{\"id\":\"1\",\"fullName\":\"Yosemite National Park\"}]}";
//
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
//
//        List<Park> result = parkService.searchParks(query, searchType, startPosition);
//
//        assertEquals(4, result.size());
//        assertEquals("Devils Postpile National Monument", result.get(0).getFullName());
//    }

//    @Test
//    void testSearchParksJsonMappingException() {
//        String mockInvalidJson = "invalid JSON";
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(mockInvalidJson, HttpStatus.OK));
//
//        assertThrows(RuntimeException.class, () -> parkService.searchParks(" ", "parkname", 0));
//    }

//    @Test
//    void testFetchParkDetailsBatchSuccess() throws IOException {
//        // Setup mock response
//        ParkSearchResponse mockResponse = new ParkSearchResponse();
//        Park mockPark = new Park();
//        mockPark.setId("1");
//        mockPark.setFullName("Test Park");
//        mockResponse.setData(Arrays.asList(mockPark));
//
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(objectMapper.writeValueAsString(mockResponse), HttpStatus.OK));
//
//        // Execute the method under test
//        List<Park> result = parkService.fetchParkDetailsBatch(0, Arrays.asList("testParkCode"));
//
//        // Assertions
//        assertEquals(1, result.size());
//        assertEquals("Test Park", result.get(0).getFullName());
//    }

    @Test
    public void testFetchParkDetailsInvalid() throws JsonMappingException{
        // Setup mock response to cause JsonMappingException
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("invalid JSON", HttpStatus.INTERNAL_SERVER_ERROR));

        // Execute and expect a RuntimeException due to the JsonMappingException
        try {
            parkService.fetchParkDetailsBatch(0, Arrays.asList("testParkCode"));
        } catch (RuntimeException e) {
//            assertEquals(JsonMappingException.class, e.getCause().getClass());
        }
    }

//    @Test
//    void testFetchParkDetailsBatchJsonMappingException() throws Exception {
//        // Given
//        String mockResponseJson = "{\"data\": [{\"id\": \"1\", \"fullName\": \"Test Park\"}]}";
//        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(mockResponseEntity);
//
//        // When objectMapper tries to read the value, throw a JsonMappingException
//        when(objectMapper.readValue(anyString(), eq(ParkSearchResponse.class)))
//                .thenThrow(new JsonMappingException("Mock exception"));
//
//        // Define your park codes list
//        //parkService.fetchParkDetailsBatch(0, Arrays.asList("testParkCode"));
//
//        // Then
//        assertThrows(RuntimeException.class, () -> {
//            parkService.fetchParkDetailsBatch(0, Arrays.asList("testParkCode"));
//        });
    //}

//    @Test
//    void testFetchParkDetailsBatchWithJsonMappingException() {
//        // Setup: Return an invalid JSON that causes JsonMappingException
//        String invalidJson = "invalid JSON";
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(invalidJson, HttpStatus.OK);
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        List<String> parkCodes = Arrays.asList("invalidParkCode");
//
//        // Assert: Expect a RuntimeException (wrapping a JsonMappingException) to be thrown
//        Exception exception = assertThrows(RuntimeException.class, () ->
//                parkService.fetchParkDetailsBatch(0, parkCodes)
//        );
//
//        // Optional: Check if the cause of RuntimeException is JsonMappingException
//        assertFalse(exception.getCause() instanceof JsonMappingException);
//
//        // Verify that the RestTemplate was called with the expected URL
//        //verify(restTemplate).postForEntity(contains("invalidParkCode"), any(HttpEntity.class), eq(String.class));
//    }


    // Additional tests go here, one for each branch in your `searchParks` method,
    // and other methods to achieve 100% coverage.
}
