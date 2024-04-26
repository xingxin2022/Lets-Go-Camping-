package edu.usc.csci310.project.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ParkServiceTest {

    private final String API_KEY = "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
    private final String BASE_URL = "https://developer.nps.gov/api/v1";


    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ParkService parkService;

    @Mock
    private PreparedStatement checkFavoriteStmt;

    @Mock
    private PreparedStatement getMaxOrderStmt;

    @Mock
    private PreparedStatement insertFavoriteStmt;

    @Mock
    private PreparedStatement getPublicStatusStmt;

    @Spy
    @InjectMocks
    private ParkService parkServiceSpy;



    @Test
    void fetchParkDetailsBatch() throws Exception {
        List<String> parkCodes = Collections.singletonList("SNP");
        String jsonResponse = "{\"data\":[{\"id\":\"1\",\"fullName\":\"Sample National Park\",\"parkCode\":\"SNP\",\"description\":\"A beautiful national park.\"}]}";

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        Park expectedPark = new Park(); // Assume appropriate setters to initialize
        expectedPark.setParkCode("SNP");
        ParkSearchResponse expectedResponse = new ParkSearchResponse();
        expectedResponse.setData(Collections.singletonList(expectedPark));

        // Mock ObjectMapper to return the expected ParkSearchResponse
        when(objectMapper.readValue(eq(jsonResponse), eq(ParkSearchResponse.class)))
                .thenReturn(expectedResponse);

        doReturn(Collections.emptyMap()).when(parkServiceSpy).fetchAmenitiesForParks(anyList());

        List<Park> result = parkServiceSpy.fetchParkDetailsBatch(0, parkCodes);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("SNP", result.get(0).getParkCode());

        // Verify fetchAmenitiesForParks was called with non-null argument
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(parkServiceSpy).fetchAmenitiesForParks(captor.capture());
        assertNotNull(captor.getValue());
        assertFalse(captor.getValue().isEmpty());
    }

    @Test
    void fetchParkDetailsBatch_JsonMappingException() throws Exception {
        String jsonResponse = "{\"data\":[]}";
        when(restTemplate.postForEntity(any(String.class), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));

        when(objectMapper.readValue(anyString(), any(Class.class)))
                .thenThrow(JsonMappingException.class);

        List<String> parkCodes = Collections.singletonList("SNP");

        // Test the method
        Exception exception = assertThrows(RuntimeException.class, () -> {
            parkService.fetchParkDetailsBatch(0, parkCodes);
        });

        assertTrue(exception.getCause() instanceof JsonMappingException);
    }

    @Test
    void testFetchParkDetailsBatch_NotSuccessfulResponse() {
        // Simulate a non-successful HTTP response
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        List<String> parkCodes = List.of("YOSE"); // Example park code
        List<Park> parks = parkService.fetchParkDetailsBatch(0, parkCodes);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the response is not successful.");
    }

    @Test
    void testFetchParkDetailsBatch_NullParkSearchResponse() throws JsonProcessingException {
        // Simulate a successful HTTP response but with null ParkSearchResponse data
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        when(objectMapper.readValue(anyString(), eq(ParkSearchResponse.class)))
                .thenReturn(null); // Return null to simulate no data

        List<String> parkCodes = List.of("YOSE");
        List<Park> parks = parkService.fetchParkDetailsBatch(0, parkCodes);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the ParkSearchResponse is null.");
    }
    @Test
    void testFetchParkDetailsBatch_NullData() throws Exception {
        // Given
        List<String> parkCodes = List.of("YOSE"); // Example park code

        ResponseEntity<String> mockResponse = new ResponseEntity<>("{}", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(mockResponse);

        ParkSearchResponse mockParkSearchResponse = mock(ParkSearchResponse.class);
        when(mockParkSearchResponse.getData()).thenReturn(null);

        when(objectMapper.readValue(anyString(), eq(ParkSearchResponse.class)))
                .thenReturn(mockParkSearchResponse);

        List<Park> result = parkService.fetchParkDetailsBatch(0, parkCodes);

        assertTrue(result.isEmpty(), "Result should be empty when no data is returned from the API.");
    }

    @Test
    void fetchActivityId() throws JSONException {
        String query = "hiking";
        String expectedId = "ACTIVITY_ID";
        String jsonResponse = "{\"data\":[{\"id\":\"" + expectedId + "\"}]}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        // When
        String actualId = parkService.fetchActivityId(query);

        // Then
        assertEquals(expectedId, actualId);
    }

    @Test
    void fetchActivityId_NoId() throws JSONException {
        String query = "nonexistentActivity";
        String jsonResponse = "{\"data\":[]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        // When
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponse);

        // Execute
        String activityId = parkService.fetchActivityId(query);

        // Then
        assertTrue(activityId.isEmpty(), "Activity ID should be empty if no activities are found.");
    }

    @Test
    void fetchAmenityId() {
        String query = "accessible";
        String expectedId = "ACTIVITY_ID";
        String jsonResponse = "{\"data\":[{\"id\":\"" + expectedId + "\"}]}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        // When
        String actualId = parkService.fetchAmenityId(query);

        // Then
        assertEquals(expectedId, actualId);
    }

    @Test
    void fetchAmenityId_NoId() {
        String query = "non-existed";
        String jsonResponse = "{\"data\":[]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), Mockito.eq(String.class))).thenReturn(responseEntity);

        // When
        String actualId = parkService.fetchAmenityId(query);

        // Then
        assertTrue(actualId.isEmpty());
    }

    @Test
    void getStateCode_ReturnsCorrectStateCode() {
        // Create a test map
        Map<String, String> testStateCodeMap = new HashMap<>();
        testStateCodeMap.put("texas", "TX");

        ParkService service = new ParkService(dataSource, restTemplate, testStateCodeMap, objectMapper);

        String stateCode = service.getStateCode("texas");
        assertEquals("TX", stateCode);
    }

    @Test
    void searchParksByParkName() throws Exception {
        String query = "Yosemite";
        String searchType = "parkname";
        int startPosition = 0;
        String mockResponseJson = "{\"data\":[{\"id\":\"1\",\"parkCode\":\"YOSE\",\"fullName\":\"Yosemite National Park\",\"description\":\"A famous national park.\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(),Mockito.eq(String.class) )).thenReturn(mockResponse);
        doReturn(Collections.emptyMap()).when(parkServiceSpy).fetchAmenitiesForParks(anyList());

        Park expectedPark = new Park(); // Assume appropriate setters to initialize
        expectedPark.setParkCode("YOSE");
        expectedPark.setFullName("Yosemite National Park");
        expectedPark.setDescription("A famous national park.");
        ParkSearchResponse expectedResponse = new ParkSearchResponse();
        expectedResponse.setData(Collections.singletonList(expectedPark));

        // Mock ObjectMapper to return the expected ParkSearchResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkSearchResponse.class)))
                .thenReturn(expectedResponse);

        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);

        assertNotNull(result);
        assertEquals(1, result.size());
        Park park = result.get(0);
        assertEquals("YOSE", park.getParkCode());
        assertEquals("Yosemite National Park", park.getFullName());

        // Verify fetchAmenitiesForParks was called with non-null argument
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(parkServiceSpy).fetchAmenitiesForParks(captor.capture());
        assertNotNull(captor.getValue());
        assertFalse(captor.getValue().isEmpty());
    }

//    @Test
//    void searchParksByState() throws Exception {
//        String query = "texas";
//        String searchType = "states";
//        int startPosition = 0;
//        String expectedStateCode = "TX";
//        String expectedUrl = "https://developer.nps.gov/api/v1/parks?limit=10&start=" +
//                startPosition + "&stateCode=" + expectedStateCode + "&api_key=" +
//                "zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        // Setup the headers, as they are used in the actual service method
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        // Create a mock response entity
//        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"data\": [] }", HttpStatus.OK);
//
//        // Stub the restTemplate.postForEntity method correctly with the HttpEntity that matches the one used in the service
//        when(restTemplate.postForEntity(eq(expectedUrl), eq(requestEntity), eq(String.class))).thenReturn(responseEntity);
//
//        // Mock the getStateCode method if it's not using the injected stateCodeMap
//        doReturn(expectedStateCode).when(parkServiceSpy).getStateCode(query);
//
//        // Act
//        List<Park> parks = parkServiceSpy.searchParks(query, searchType, startPosition);
//
//        // Assert
//        assertEquals(0, parks.size());
//    }
    @Test
    void searchParks_NotSuccessfulResponse() {
        String query = "Yosemite";
        String searchType = "parkname";
        int startPosition = 0;
        // Simulate a non-successful HTTP response
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(),Mockito.eq(String.class) ))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        List<String> parkCodes = List.of("YOSE"); // Example park code
        List<Park> parks = parkService.searchParks(query, searchType, startPosition);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the response is not successful.");
    }
    @Test
    void searchParks_NullData() throws Exception {
        String query = "Yosemite";
        String searchType = "parkname";
        int startPosition = 0;
        String mockResponseJson = "{\"data\":[{\"id\":\"1\",\"parkCode\":\"YOSE\",\"fullName\":\"Yosemite National Park\",\"description\":\"A famous national park.\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(),Mockito.eq(String.class) )).thenReturn(mockResponse);

        // Mock ObjectMapper to return the expected ParkSearchResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkSearchResponse.class)))
                .thenReturn(null);

        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);

        assertTrue(result.isEmpty(), "The result should be empty if the ParkActivityResponse is null.");
    }

    @Test
    void searchParksByAmenities() throws Exception {
        doReturn("amenity1").when(parkServiceSpy).fetchAmenityId(anyString());

        String query = "camping";
        String searchType = "amenities";
        int startPosition = 0;
        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]]}";
        Park mockPark = new Park();
        mockPark.setParkCode("YOSE");
        mockPark.setFullName("Yosemite National Park");
        List<Park> mockParkList = Collections.singletonList(mockPark);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());

        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        detailedAmenityPark.setParkCode("YOSE");
        detailedAmenityPark.setFullName("Yosemite National Park");

        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setId("amenity1");
        amenityPark.setName("Camping");
        amenityPark.setAmenityParks(Collections.singletonList(detailedAmenityPark));

        ParkAmenityResponse expectedResponse = new ParkAmenityResponse();
        expectedResponse.setData(Collections.singletonList(Collections.singletonList(amenityPark)));

        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkAmenityResponse.class)))
                .thenReturn(expectedResponse);

        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
        assertNotNull(result);
        assertEquals(1, result.size());
        Park park = result.get(0);
        assertEquals("YOSE", park.getParkCode());
        assertEquals("Yosemite National Park", park.getFullName());
    }
    @Test
    void searchParksByAmenities_NullData() throws Exception {
        doReturn("amenity1").when(parkServiceSpy).fetchAmenityId(anyString());

        String query = "camping";
        String searchType = "amenities";
        int startPosition = 0;
        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]]}";

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);


        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkAmenityResponse.class)))
                .thenReturn(null);


        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);

        assertTrue(result.isEmpty(), "The result should be empty if the ParkActivityResponse is null.");
    }
    @Test
    void searchParksByAmenities_NoId() throws Exception {
        doReturn(null).when(parkServiceSpy).fetchAmenityId(anyString());

        String query = "camping";
        String searchType = "amenities";
        int startPosition = 0;
        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]]}";
        Park mockPark = new Park();
        mockPark.setParkCode("YOSE");
        mockPark.setFullName("Yosemite National Park");
        List<Park> mockParkList = Collections.singletonList(mockPark);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());

        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        detailedAmenityPark.setParkCode("YOSE");
        detailedAmenityPark.setFullName("Yosemite National Park");

        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setId("amenity1");
        amenityPark.setName("Camping");
        amenityPark.setAmenityParks(Collections.singletonList(detailedAmenityPark));

        ParkAmenityResponse expectedResponse = new ParkAmenityResponse();
        expectedResponse.setData(Collections.singletonList(Collections.singletonList(amenityPark)));

        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkAmenityResponse.class)))
                .thenReturn(expectedResponse);

        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
        assertNotNull(result);
        assertEquals(1, result.size());
        Park park = result.get(0);
        assertEquals("YOSE", park.getParkCode());
        assertEquals("Yosemite National Park", park.getFullName());

    }

    @Test
    void searchParksByAmenities_EmptyId() throws Exception {
        doReturn("").when(parkServiceSpy).fetchAmenityId(anyString());

        String query = "camping";
        String searchType = "amenities";
        int startPosition = 0;
        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]]}";
        Park mockPark = new Park();
        mockPark.setParkCode("YOSE");
        mockPark.setFullName("Yosemite National Park");
        List<Park> mockParkList = Collections.singletonList(mockPark);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());

        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        detailedAmenityPark.setParkCode("YOSE");
        detailedAmenityPark.setFullName("Yosemite National Park");

        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setId("amenity1");
        amenityPark.setName("Camping");
        amenityPark.setAmenityParks(Collections.singletonList(detailedAmenityPark));

        ParkAmenityResponse expectedResponse = new ParkAmenityResponse();
        expectedResponse.setData(Collections.singletonList(Collections.singletonList(amenityPark)));

        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkAmenityResponse.class)))
                .thenReturn(expectedResponse);

        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
        assertNotNull(result);
        assertEquals(1, result.size());
        Park park = result.get(0);
        assertEquals("YOSE", park.getParkCode());
        assertEquals("Yosemite National Park", park.getFullName());

    }

    @Test
    void searchParksByAmenities_NonZeroStart() throws Exception {
        doReturn("amenity1").when(parkServiceSpy).fetchAmenityId(anyString());

        String query = "camping";
        String searchType = "amenities";
        int initialStartPosition = 0;
//        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]]}";
        String mockResponseJson = "{\"data\": [[{\"id\": \"amenity1\", \"name\": \"Camping\", \"parks\": [" +
                IntStream.range(0, 11).mapToObj(i -> "{\"parkCode\": \"PARK" + i + "\", \"fullName\": \"Park " + i + "\"}").collect(Collectors.joining(",")) +
                "]}]]}";
        Park mockPark = new Park();
        mockPark.setParkCode("YOSE");
        mockPark.setFullName("Yosemite National Park");
        List<Park> mockParkList = Collections.singletonList(mockPark);

        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(eq(initialStartPosition), anyList());

        List<AmenityPark.DetailedAmenityPark> detailedAmenityParks = IntStream.range(0, 11)
                .mapToObj(i -> {
                    AmenityPark.DetailedAmenityPark park = new AmenityPark.DetailedAmenityPark();
                    park.setParkCode("PARK" + i);
                    park.setFullName("Park " + i);
                    return park;
                })
                .collect(Collectors.toList());

        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setId("amenity1");
        amenityPark.setName("Camping");
        amenityPark.setAmenityParks(detailedAmenityParks);

        ParkAmenityResponse expectedResponse = new ParkAmenityResponse();
        expectedResponse.setData(Collections.singletonList(Collections.singletonList(amenityPark)));

        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkAmenityResponse.class)))
                .thenReturn(expectedResponse);


        parkServiceSpy.searchParks(query, searchType, initialStartPosition);

        int testStartPosition = 10;
        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(eq(testStartPosition), anyList());

        List<Park> result = parkServiceSpy.searchParks(query, searchType, testStartPosition);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        Park park = result.get(0);
        assertEquals("YOSE", park.getParkCode());
        assertEquals("Yosemite National Park", park.getFullName());
    }

//    @Test
//    void searchParksByActivities() throws Exception {
//        String activityId = "activity1";
//        doReturn(activityId).when(parkServiceSpy).fetchActivityId(anyString());
//
//        String query = "hiking";
//        String searchType = "activities";
//        int startPosition = 0;
//        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]}";
//        Park mockPark = new Park();
//        mockPark.setParkCode("YOSE");
//        mockPark.setFullName("Yosemite National Park");
//        List<Park> mockParkList = Collections.singletonList(mockPark);
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        String activityParksUrl = "https://developer.nps.gov/api/v1/activities/parks?limit=50&id=" + activityId + "&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        // Setup the headers, as they are used in the actual service method
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        when(restTemplate.postForEntity(eq(activityParksUrl), eq(requestEntity), eq(String.class))).thenReturn(mockResponse);
//
////        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
//        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());
//
//
//        ActivityPark.DetailedActivityPark detailedActivityPark = new ActivityPark.DetailedActivityPark();
//        detailedActivityPark.setParkCode("YOSE");
//        detailedActivityPark.setFullName("Yosemite National Park");
//
//        ActivityPark activityPark = new ActivityPark();
//        activityPark.setId("activity1");
//        activityPark.setName("Hiking");
//        activityPark.setActivityParks(Collections.singletonList(detailedActivityPark));
//
//        ParkActivityResponse expectedResponse = new ParkActivityResponse();
//        expectedResponse.setData(Collections.singletonList(activityPark));
//
//        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
//        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkActivityResponse.class)))
//                .thenReturn(expectedResponse);
//
//        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Park park = result.get(0);
//        assertEquals("YOSE", park.getParkCode());
//        assertEquals("Yosemite National Park", park.getFullName());
//    }
//    @Test
//    void searchParksByActivities_NoId() throws Exception {
//        doReturn(null).when(parkServiceSpy).fetchActivityId(anyString());
//
//        String query = "hiking";
//        String searchType = "activities";
//        int startPosition = 0;
//        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]}";
//        Park mockPark = new Park();
//        mockPark.setParkCode("YOSE");
//        mockPark.setFullName("Yosemite National Park");
//        List<Park> mockParkList = Collections.singletonList(mockPark);
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        String activityParksUrl = "https://developer.nps.gov/api/v1/activities/parks?limit=50&q=" + query + "&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        when(restTemplate.postForEntity(eq(activityParksUrl), eq(requestEntity), eq(String.class))).thenReturn(mockResponse);
//
//        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());
//
//
//        ActivityPark.DetailedActivityPark detailedActivityPark = new ActivityPark.DetailedActivityPark();
//        detailedActivityPark.setParkCode("YOSE");
//        detailedActivityPark.setFullName("Yosemite National Park");
//
//        ActivityPark activityPark = new ActivityPark();
//        activityPark.setId("activity1");
//        activityPark.setName("Hiking");
//        activityPark.setActivityParks(Collections.singletonList(detailedActivityPark));
//
//        ParkActivityResponse expectedResponse = new ParkActivityResponse();
//        expectedResponse.setData(Collections.singletonList(activityPark));
//
//        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
//        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkActivityResponse.class)))
//                .thenReturn(expectedResponse);
//
//        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Park park = result.get(0);
//        assertEquals("YOSE", park.getParkCode());
//        assertEquals("Yosemite National Park", park.getFullName());
//    }
//    @Test
//    void searchParksByActivities_EmptyId() throws Exception {
//        doReturn("").when(parkServiceSpy).fetchActivityId(anyString());
//
//        String query = "hiking";
//        String searchType = "activities";
//        int startPosition = 0;
//        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]}";
//        Park mockPark = new Park();
//        mockPark.setParkCode("YOSE");
//        mockPark.setFullName("Yosemite National Park");
//        List<Park> mockParkList = Collections.singletonList(mockPark);
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        String activityParksUrl = "https://developer.nps.gov/api/v1/activities/parks?limit=50&q=" + query + "&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        when(restTemplate.postForEntity(eq(activityParksUrl), eq(requestEntity), eq(String.class))).thenReturn(mockResponse);
//
//        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());
//
//        ActivityPark.DetailedActivityPark detailedActivityPark = new ActivityPark.DetailedActivityPark();
//        detailedActivityPark.setParkCode("YOSE");
//        detailedActivityPark.setFullName("Yosemite National Park");
//
//        ActivityPark activityPark = new ActivityPark();
//        activityPark.setId("activity1");
//        activityPark.setName("Hiking");
//        activityPark.setActivityParks(Collections.singletonList(detailedActivityPark));
//
//        ParkActivityResponse expectedResponse = new ParkActivityResponse();
//        expectedResponse.setData(Collections.singletonList(activityPark));
//
//        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
//        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkActivityResponse.class)))
//                .thenReturn(expectedResponse);
//
//        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        Park park = result.get(0);
//        assertEquals("YOSE", park.getParkCode());
//        assertEquals("Yosemite National Park", park.getFullName());
//    }
//    @Test
//    void searchParksByActivities_NullData() throws Exception {
//        String activityId = "activity1";
//        doReturn(activityId).when(parkServiceSpy).fetchActivityId(anyString());
//
//        String query = "hiking";
//        String searchType = "activities";
//        int startPosition = 0;
//        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]}";
//
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        String activityParksUrl = "https://developer.nps.gov/api/v1/activities/parks?limit=50&id=" + activityId + "&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        when(restTemplate.postForEntity(eq(activityParksUrl), eq(requestEntity), eq(String.class))).thenReturn(mockResponse);
//
//        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkActivityResponse.class)))
//                .thenReturn(null);
//
//        List<Park> result = parkServiceSpy.searchParks(query, searchType, startPosition);
//
//        assertTrue(result.isEmpty(), "The result should be empty if the ParkActivityResponse is null.");
//    }



//    @Test
//    void searchParksByActivities_NonZeroStart() throws Exception {
//        String activityId = "activity1";
//        doReturn(activityId).when(parkServiceSpy).fetchActivityId(anyString());
//
//        String query = "hiking";
//        String searchType = "activities";
//        int initialStartPosition = 0;
////        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [{\"parkCode\": \"YOSE\", \"fullName\": \"Yosemite National Park\"}]}]}";
//        String mockResponseJson = "{\"data\": [{\"id\": \"activity1\", \"name\": \"Hiking\", \"parks\": [" +
//                IntStream.range(0, 11).mapToObj(i -> "{\"parkCode\": \"PARK" + i + "\", \"fullName\": \"Park " + i + "\"}").collect(Collectors.joining(",")) +
//                "]}]}";
//        Park mockPark = new Park();
//        mockPark.setParkCode("YOSE");
//        mockPark.setFullName("Yosemite National Park");
//        List<Park> mockParkList = Collections.singletonList(mockPark);
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);
//        String activityParksUrl = "https://developer.nps.gov/api/v1/activities/parks?limit=50&id=" + activityId + "&api_key=zAU4RYdbLdkC6aM98RBnYuu2mEP3THiadaGz3LTe";
//
//        // Setup the headers, as they are used in the actual service method
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//
//        when(restTemplate.postForEntity(eq(activityParksUrl), eq(requestEntity), eq(String.class))).thenReturn(mockResponse);
//
////        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class))).thenReturn(mockResponse);
//        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(anyInt(), anyList());
//
//        List<ActivityPark.DetailedActivityPark> detailedActivityParks = IntStream.range(0, 11)
//                .mapToObj(i -> {
//                    ActivityPark.DetailedActivityPark park = new ActivityPark.DetailedActivityPark();
//                    park.setParkCode("PARK" + i);
//                    park.setFullName("Park " + i);
//                    return park;
//                })
//                .collect(Collectors.toList());
//
//        ActivityPark activityPark = new ActivityPark();
//        activityPark.setId("amenity1");
//        activityPark.setName("Camping");
//        activityPark.setActivityParks(detailedActivityParks);
//
//        ParkActivityResponse expectedResponse = new ParkActivityResponse();
//        expectedResponse.setData(Collections.singletonList(activityPark));
//
//        // Correctly mock ObjectMapper to return the expected ParkAmenityResponse
//        when(objectMapper.readValue(eq(mockResponseJson), eq(ParkActivityResponse.class)))
//                .thenReturn(expectedResponse);
//
//        parkServiceSpy.searchParks(query, searchType, initialStartPosition);
//        int testStartPosition = 10;
//        doReturn(mockParkList).when(parkServiceSpy).fetchParkDetailsBatch(eq(testStartPosition), anyList());
//
//
//        List<Park> result = parkServiceSpy.searchParks(query, searchType, testStartPosition);
//        assertNotNull(result);
//        assertTrue( result.size() > 0);
//        Park park = result.get(0);
//        assertEquals("YOSE", park.getParkCode());
//        assertEquals("Yosemite National Park", park.getFullName());
//    }


    @Test
    void searchParks_JsonProcessingException() throws Exception{
        // Given
        String parkCodesJson = "{\"data\":[]}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(parkCodesJson, HttpStatus.OK));

        // Mock ObjectMapper to throw JsonProcessingException
        when(objectMapper.readValue(anyString(), any(Class.class)))
                .thenThrow(new JsonMappingException(null, "Simulated JSON error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            parkService.searchParks("query", "parkname", 0);
        });
    }

//    @Test
//    void fetchAmenitiesForParks_ThrowsRuntimeException_WhenJsonProcessingExceptionOccurs() throws Exception {
//        List<String> parkCodes = Collections.singletonList("SNP");
//        String mockResponse = "{\"data\":[]}"; // Adjust as necessary for your case
//
//        // Mock restTemplate to return a successful response entity
//        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
//
//        // Force objectMapper to throw a JsonMappingException
//        when(objectMapper.readValue(anyString(), eq(AmenityPerParkResponse.class)))
//                .thenThrow(new JsonMappingException(null, "Simulated parsing error"));
//
//        // Execute the method and expect a RuntimeException due to the simulated JsonProcessingException
//        Exception exception = assertThrows(RuntimeException.class, () -> parkService.fetchAmenitiesForParks(parkCodes));
//
//        // Verify that the RuntimeException is due to a JsonProcessingException
//        assertTrue(exception.getCause() instanceof JsonMappingException, "Expected cause to be JsonMappingException");
//
//        // Verify objectMapper interaction to ensure the mock setup was effective
//        verify(objectMapper).readValue(eq(mockResponse), eq(AmenityPerParkResponse.class));
//    }


    @Test
    void fetchAmenitiesForParks() throws Exception {
        List<String> parkCodes = Arrays.asList("parkCode1", "parkCode2");
        String mockResponseJson = "{\"data\":[{\"id\":\"amenity1\",\"categories\":[\"category1\"],\"name\":\"Amenity One\"}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseJson, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), Mockito.eq(String.class)))
                .thenReturn(mockResponse);

        Park.Amenity expectedAmenity = new Park.Amenity();
        expectedAmenity.setId("amenity1");
        expectedAmenity.setName("Accessible");
        expectedAmenity.setCategories(List.of("Accessibility"));

        // Assuming AmenityPerParkResponse directly contains a list of Park.Amenity
        AmenityPerParkResponse expectedResponse = new AmenityPerParkResponse();
        expectedResponse.setData(List.of(expectedAmenity));

        when(objectMapper.readValue(eq(mockResponseJson), eq(AmenityPerParkResponse.class)))
                .thenReturn(expectedResponse);

        Map<String, List<Park.Amenity>> result = parkService.fetchAmenitiesForParks(parkCodes);

        assertEquals(parkCodes.size(), result.size(), "The size of the result should match the number of park codes.");

        parkCodes.forEach(parkCode -> {
            assertTrue(result.containsKey(parkCode), "The result should contain the park code.");
            assertEquals(1, result.get(parkCode).size(), "Each park should have one amenity.");
            assertEquals("amenity1", result.get(parkCode).get(0).getId(), "The amenity ID should match.");
        });
    }

    @Test
    void fetchAmenitiesForParks_JsonMappingException() throws IOException {
        List<String> parkCodes = Collections.singletonList("PARK123");
        String jsonResponse = "{\"data\":[]}"; // Simplified JSON response

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));
        when(objectMapper.readValue(eq(jsonResponse), eq(AmenityPerParkResponse.class)))
                .thenThrow(new JsonMappingException(null, "Simulated JsonMappingException"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            parkService.fetchAmenitiesForParks(parkCodes);
        });

        assertTrue(exception.getCause() instanceof JsonMappingException);
    }

    //    @Test
//    void fetchAmenitiesForParks_RuntimeException() throws Exception {
//        List<String> parkCodes = Collections.singletonList("PARK123");
//        String jsonResponse = "{\"data\":[]}";
//
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(new ResponseEntity<>(jsonResponse, HttpStatus.OK));
//        when(objectMapper.readValue(eq(jsonResponse), eq(AmenityPerParkResponse.class)))
//                .thenThrow(new RuntimeException("Simulated RuntimeException due to IOException"));
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            parkService.fetchAmenitiesForParks(parkCodes);
//        });
//
//        assertTrue(exception.getMessage().contains("Simulated RuntimeException due to IOException"));
//    }
//    @Test
//    void fetchAmenitiesForParks_JsonProcessingException() throws Exception {
//        // Setup
//        List<String> parkCodes = Collections.singletonList("PARK123");
//        String jsonResponse = "{\"data\":[]}"; // Example JSON response
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
//
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(mockResponse);
//        // Since IOException is a checked exception, wrap it in a RuntimeException
//        when(objectMapper.readValue(eq(jsonResponse), eq(AmenityPerParkResponse.class)))
//                .thenAnswer(invocation -> {
//                    throw new RuntimeException(new IOException("Simulated exception for testing"));
//                });
//
//        // Execute & Verify
//        Exception exception = assertThrows(RuntimeException.class, () -> parkService.fetchAmenitiesForParks(parkCodes));
//        assertNotNull(exception);
//        assertTrue(exception.getCause() instanceof IOException);
//    }
    @Test
    void fetchAmenitiesForParks_NotSuccessfulResponse() {
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        List<String> parkCodes = Collections.singletonList("PARK123");
        Map<String, List<Park.Amenity>> parks = parkService.fetchAmenitiesForParks(parkCodes);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the response is not successful.");
    }

    @Test
    void fetchAmenitiesForParks_NullParkSearchResponse() throws JsonProcessingException {
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        when(objectMapper.readValue(anyString(), eq(AmenityPerParkResponse.class)))
                .thenReturn(null);

        List<String> parkCodes = Collections.singletonList("PARK123");
        Map<String, List<Park.Amenity>> parks = parkService.fetchAmenitiesForParks(parkCodes);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the ParkSearchResponse is null.");
    }
    @Test
    void fetchAmenitiesForParks_NullData() throws Exception {
        // Given
        List<String> parkCodes = Collections.singletonList("PARK123");

        ResponseEntity<String> mockResponse = new ResponseEntity<>("{}", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class)))
                .thenReturn(mockResponse);

        AmenityPerParkResponse mockAmenityPerParkResponse = mock(AmenityPerParkResponse.class);
        when(mockAmenityPerParkResponse.getData()).thenReturn(null);

        when(objectMapper.readValue(anyString(), eq(AmenityPerParkResponse.class)))
                .thenReturn(mockAmenityPerParkResponse);

        Map<String, List<Park.Amenity>> parks = parkService.fetchAmenitiesForParks(parkCodes);

        assertTrue(parks.isEmpty(), "Result should be empty when no data is returned from the API.");
    }



    @Test
    void addFavorite() throws SQLException {
        String userName = "user";
        String parkCode = "park1";
        String parkName = "park1";
        boolean isPrivate = false;
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt).thenReturn(getMaxOrderStmt)
                .thenReturn(getPublicStatusStmt).thenReturn(insertFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(0);
        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);
        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0); // No existing favorite, maxOrder = 10

        // When
        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);

        // Then
        assertEquals("Park successfully added to favorite list", response.getMessage());
    }
    @Test
    void addFavorite_ResourceCloseException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt)
                .thenReturn(getPublicStatusStmt)
                .thenReturn(getMaxOrderStmt)
                .thenReturn(insertFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(0);

        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0); // No existing favorite, maxOrder = 10
        when(insertFavoriteStmt.executeUpdate()).thenReturn(0);
        doThrow(new SQLException("ResultSet close error")).when(resultSet).close();
        doThrow(new SQLException("CheckFavoriteStmt close error")).when(checkFavoriteStmt).close();
        doThrow(new SQLException("GetPublicStatusStmt close error")).when(getPublicStatusStmt).close();
        doThrow(new SQLException("GetMaxOrderStmt close error")).when(getMaxOrderStmt).close();
        doThrow(new SQLException("InsertFavoriteStmt close error")).when(insertFavoriteStmt).close();
        doThrow(new SQLException("Connection close error")).when(connection).close();

        parkService.addFavorite("user", "parkCode", "parkName", false);

        verify(resultSet).close();
        verify(checkFavoriteStmt).close();
        verify(getPublicStatusStmt).close();
        verify(getMaxOrderStmt).close();
        verify(insertFavoriteStmt).close();
        verify(connection).close();
    }


    @Test
    void addFavorite_NoRowImpacted() throws SQLException {

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt).thenReturn(getMaxOrderStmt)
                .thenReturn(getPublicStatusStmt).thenReturn(insertFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(0);
        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        when(insertFavoriteStmt.executeUpdate()).thenReturn(1);

        // When
        FavoriteResponse result = parkService.addFavorite("user", "parkCode", "parkName", false);

        // Then
        assertNotNull(result);
        assertEquals("Park successfully added to favorite list", result.getMessage());
    }
    @Test
    void addFavorite_TruePrivate() throws SQLException {

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt).thenReturn(getPublicStatusStmt)
                .thenReturn(getMaxOrderStmt)
                .thenReturn(insertFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(0);
        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        when(insertFavoriteStmt.executeUpdate()).thenReturn(1);
        when(resultSet.getInt(1)).thenReturn(0);
        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        when(insertFavoriteStmt.executeUpdate()).thenReturn(0);

        // When
        FavoriteResponse result = parkService.addFavorite("user", "parkCode", "parkName", true);

        // Then
        assertNotNull(result);
    }
    @Test
    void addFavorite_insertFail() throws SQLException {

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt).
                thenReturn(getPublicStatusStmt).thenReturn(getMaxOrderStmt).thenReturn(insertFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(0);
        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        doThrow(new SQLTimeoutException("Timeout occurred")).when(insertFavoriteStmt).executeUpdate();

        FavoriteResponse result = parkService.addFavorite("user", "parkCode", "parkName", false);

        assertNotNull(result);
        assertEquals("Error when adding favorite park: Timeout occurred", result.getMessage());
    }
    @Test
    void addFavorite_NoMaxOrderFound() throws SQLException {
        String userName = "user";
        String parkCode = "park2";
        String parkName = "park1";
        boolean isPrivate = true;

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt, getMaxOrderStmt,
                getPublicStatusStmt,insertFavoriteStmt);

        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Park not in favorites

        when(getPublicStatusStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        when(getMaxOrderStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // No max order

        when(insertFavoriteStmt.executeUpdate()).thenReturn(1); // 1 row updated

        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);
        assertEquals("Park successfully added to favorite list", response.getMessage());
    }

    @Test
    void addFavorite_MaxOrderSQLTimeoutException() throws SQLException {
        String userName = "user";
        String parkCode = "park3";
        String parkName = "park1";
        boolean isPrivate = true;

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt, getMaxOrderStmt);

        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Park not in favorites

        when(getMaxOrderStmt.executeQuery()).thenThrow(new SQLException("Query timeout"));

        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);
        assertTrue(response.getMessage().contains("Error when adding favorite park"), "Response message should indicate a timeout error");
    }

    @Test
    void addFavorite_ParkExitsSQLTimeoutException() throws SQLException {
        String userName = "user";
        String parkCode = "park3";
        String parkName = "park1";
        boolean isPrivate = true;

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt, getMaxOrderStmt);

        when(checkFavoriteStmt.executeQuery()).thenThrow(new SQLException("Query timeout"));

        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);
        assertNotNull(response);
        assertTrue(response.getMessage().contains("Error when adding favorite park"), "Response message should indicate a timeout error");
    }



    @Test
    public void addFavoriteAlreadyExistsTest() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(checkFavoriteStmt);
        when(checkFavoriteStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        FavoriteResponse response = parkService.addFavorite("user", "park1", "park1", true);

        // Then
        assertEquals("Park already in the favorite list", response.getMessage());

        verify(insertFavoriteStmt, never()).executeUpdate();
    }


    @Test
    void addFavorite_SQLTimeoutException() throws SQLException {
        // Arrange
        String userName = "userTest";
        String parkCode = "park123";
        String parkName = "park1";
        boolean isPrivate = true;

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLTimeoutException("Connection timeout"));

        // Act
        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);

        // Assert
        assertEquals("Error when adding favorite park: Connection timeout", response.getMessage(), "Response message should indicate an error due to timeout");
    }

    @Test
    void addFavorite_SQLTimeoutException1() throws SQLException {
        // Given
        String userName = "userTest";
        String parkCode = "park123";
        String parkName = "park1";
        boolean isPrivate = true;
        SQLTimeoutException exception = new SQLTimeoutException("Connection timeout");

        // When
        when(dataSource.getConnection()).thenThrow(exception);

        // Then
        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);
        assertEquals("Error when adding favorite park: Connection timeout", response.getMessage());
    }



    @Test
    void addFavorite_SQLException() throws SQLException {
        String userName = "userTest";
        String parkCode = "park123";
        String parkName = "park1";
        boolean isPrivate = true;

        // Simulate SQLException when attempting to prepare a statement
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("SQL error occurred"));

        // Act
        FavoriteResponse response = parkService.addFavorite(userName, parkCode, parkName, isPrivate);

        // Assert
        assertNotNull(response);
        assertTrue(response.getMessage().contains("Error when adding favorite park"), "Response message should indicate an SQL error");
    }





    @Test
    void getFavoriteParkCodesSuccess() throws SQLException {
        String userName = "testUser";
        String parkCode1 = "parkCode1";
        String parkCode2 = "parkCode2";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Simulating two results
        when(resultSet.getString("parkCode")).thenReturn(parkCode1).thenReturn(parkCode2);

        List<String> favoriteParkCodes = parkService.getFavoriteParkCodes(userName);

        assertEquals(2, favoriteParkCodes.size());
        assertEquals(parkCode1, favoriteParkCodes.get(0));
        assertEquals(parkCode2, favoriteParkCodes.get(1));
    }

    @Test
    void getFavoriteParkCodesEmpty() throws SQLException {
        String userName = "testUser";
        String parkCode1 = "parkCode1";
        String parkCode2 = "parkCode2";

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<String> favoriteParkCodes = parkService.getFavoriteParkCodes(userName);

        assertTrue(favoriteParkCodes.isEmpty(), "The list should be empty when there are no favorite parks.");
    }

    @Test
    void getFavoriteParkCodes_SQLTimeoutException() throws Exception {
        String userName = "testUser";

        when(dataSource.getConnection()).thenThrow(new SQLTimeoutException("Timeout occurred"));

        List<String> result = parkService.getFavoriteParkCodes(userName);

        assertTrue(result.isEmpty(), "Result should be empty when SQLTimeoutException occurs");
    }
    @Test
    void getFavoriteParkCodes_SQLException() throws Exception {
        String userName = "testUser";

        when(dataSource.getConnection()).thenThrow(new SQLException("Error occurred"));

        List<String> result = parkService.getFavoriteParkCodes(userName);

        assertTrue(result.isEmpty(), "Result should be empty when SQLException occurs");
    }


    @Test
    void initializeParkDatabase() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);

        parkService.initializeParkDatabase();

        verify(statement, times(2)).execute(anyString());
    }

    @Test
    public void initializeDatabaseSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenThrow(new SQLException("Database initialization error"));

        parkServiceSpy.initializeParkDatabase();
    }

}
