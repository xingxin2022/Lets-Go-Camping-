package edu.usc.csci310.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usc.csci310.project.search.AmenityPerParkResponse;
import edu.usc.csci310.project.search.Park;
import edu.usc.csci310.project.search.ParkSearchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    @InjectMocks
    private FavoriteService favoriteServiceSpy;

    @Test
    public void moveUp_Success() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Arrange
        String username = "testUser";
        String parkCode = "park123";
        int currentOrder = 2;
        int swapOrder = 1; // Moving up
        String swapParkCode = "park124";

        // Setting up the mock to handle the select queries
        when(resultSet.getInt("parkOrder")).thenReturn(currentOrder);
        when(resultSet.getString("parkCode")).thenReturn(swapParkCode);

        // Count of parks
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(2); // Total 2 parks in the list

        // Act
        favoriteServiceSpy.movePark(username, parkCode, true);

        // Verify the prepared statements are set correctly for the swap
        InOrder inOrder = inOrder(preparedStatement);
        inOrder.verify(preparedStatement).setString(1, username);
        inOrder.verify(preparedStatement).setString(2, parkCode);
        inOrder.verify(preparedStatement).setString(1, username);
        inOrder.verify(preparedStatement).setInt(2, currentOrder - 1); // Move up
        inOrder.verify(preparedStatement).setInt(1, currentOrder - 1);
        inOrder.verify(preparedStatement).setString(2, username);
        inOrder.verify(preparedStatement).setString(3, parkCode);
        inOrder.verify(preparedStatement).setInt(1, currentOrder);
        inOrder.verify(preparedStatement).setString(2, username);
        inOrder.verify(preparedStatement).setString(3, swapParkCode);

        // Assertions to ensure all interactions are accounted for
        verify(preparedStatement, times(2)).executeUpdate();
    }


    @Test
    public void moveDown_Success() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Arrange
        String username = "testUser";
        String parkCode = "park123";
        int currentOrder = 1;
        int swapOrder = 2; // Moving up
        String swapParkCode = "park124";

        // Setting up the mock to handle the select queries
        when(resultSet.getInt("parkOrder")).thenReturn(currentOrder);
        when(resultSet.getString("parkCode")).thenReturn(swapParkCode);

        // Count of parks
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(2); // Total 2 parks in the list

        // Act
        favoriteServiceSpy.movePark(username, parkCode, false);

        // Verify the prepared statements are set correctly for the swap
        InOrder inOrder = inOrder(preparedStatement);
        inOrder.verify(preparedStatement).setString(1, username);
        inOrder.verify(preparedStatement).setString(2, parkCode);
        inOrder.verify(preparedStatement).setString(1, username);
        inOrder.verify(preparedStatement).setInt(2, currentOrder + 1); // Move up
        inOrder.verify(preparedStatement).setInt(1, currentOrder + 1);
        inOrder.verify(preparedStatement).setString(2, username);
        inOrder.verify(preparedStatement).setString(3, parkCode);

        // Assertions to ensure all interactions are accounted for
        verify(preparedStatement, times(2)).executeUpdate();
    }

//    @Test
//    public void moveUp_AtTop() throws Exception {
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//        // Arrange
//        String username = "testUser";
//        String parkCode = "park123";
//        int currentOrder = 1;
//
//        // Setting up the mock to handle the select queries
//        when(resultSet.getInt("parkOrder")).thenReturn(currentOrder);
//
//        // Count of parks
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.getInt(1)).thenReturn(2); // Total 2 parks in the list
//
//        // Act
//        Exception exception = assertThrows(SQLException.class, () ->
//                favoriteServiceSpy.movePark(username, parkCode, true));
//
//        assertEquals("Failed to move park: Park is already at the top of the list", exception.getMessage());
//    }

//    @Test
//    public void moveDown_Error1() throws Exception {
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//        // Arrange
//        String username = "testUser";
//        String parkCode = "park123";
//        int currentOrder = 2;
//
//        // Setting up the mock to handle the select queries
//        when(resultSet.getInt("parkOrder")).thenReturn(currentOrder);
//
//        // Count of parks
//        when(preparedStatement.executeQuery()).thenReturn(resultSet);
//        when(resultSet.getInt(1)).thenReturn(2); // Total 2 parks in the list
//
//        // Act
//        Exception exception = assertThrows(SQLException.class, () ->
//                favoriteServiceSpy.movePark(username, parkCode, false));
//
//        assertEquals("Failed to move park: Error retrieving count of parks", exception.getMessage());
//    }

    @Test
    public void movePark_SQLExceptionDuringUpdate() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Arrange
        String username = "testUser";
        String parkCode = "park123";
        int currentOrder = 2;
        int swapOrder = 1; // Moving up
        String swapParkCode = "park124";

        // Setting up the mock to handle the select queries
        when(resultSet.getInt("parkOrder")).thenReturn(currentOrder);
        when(resultSet.getString("parkCode")).thenReturn(swapParkCode);

        // Count of parks
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(2); // Total 2 parks in the list

        // Update prepared statement throws an exception
        doThrow(new SQLException("Update failed")).when(preparedStatement).executeUpdate();

        // Act
        Exception exception = assertThrows(SQLException.class, () ->
                favoriteServiceSpy.movePark(username, parkCode, true));

        assertEquals("Failed to move park: Update failed", exception.getMessage());
    }


    @Test
    public void updateFavoriteListPrivacy_Success() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        favoriteServiceSpy.updateFavoriteListPrivacy("username", true);

        verify(preparedStatement).setBoolean(1, true);
        verify(preparedStatement).setString(2, "username");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void updateFavoriteListPrivacy_Failure() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doThrow(new SQLException("Update failed")).when(preparedStatement).executeUpdate();

        Exception exception = assertThrows(SQLException.class, () ->
                favoriteServiceSpy.updateFavoriteListPrivacy("username", true));

        assertEquals("Failed to update privacy setting: Update failed", exception.getMessage());
    }

    @Test
    public void getFavoriteParks_Success() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, true, false);
        when(resultSet.getString("parkCode")).thenReturn("parkCode1");
        when(resultSet.getString("parkName")).thenReturn("Park Name1");
        when(resultSet.getString("parkCode")).thenReturn("parkCode2");
        when(resultSet.getString("parkName")).thenReturn("Park Name2");
        when(resultSet.getString("parkCode")).thenReturn("parkCode3");
        when(resultSet.getString("parkName")).thenReturn("Park Name3");


        FavoriteResponse response = favoriteServiceSpy.getFavorites("username");
        assertEquals(3, response.getParks().size()); // Verify one park is returned
    }

    @Test
    public void getFavoriteParks_Empty() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        FavoriteResponse response = favoriteServiceSpy.getFavorites("username");
        assertEquals(response.getMessage(), "No favorite parks found for user: username");
    }

    @Test
    public void getFavoriteParks_Failure() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doThrow(new SQLException("Failed to get favorite parks")).when(preparedStatement).executeQuery();

        Exception exception = assertThrows(RuntimeException.class, () ->
                favoriteServiceSpy.getFavorites("username"));

        assertEquals("Failed to get favorite parks: Failed to get favorite parks", exception.getMessage());
    }

    @Test
    public void deleteAll_Success() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        favoriteServiceSpy.deleteAll("username");

        verify(preparedStatement).setString(1, "username");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void deleteAll_Failure() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doThrow(new SQLException("Deletion failed")).when(preparedStatement).executeUpdate();

        Exception exception = assertThrows(SQLException.class, () ->
                favoriteServiceSpy.deleteAll("username"));

        assertEquals("Failed to delete all parks: Deletion failed", exception.getMessage());
    }

    @Test
    public void testRemovePark_Successful() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        favoriteServiceSpy.removePark("testUser", "park123");

        verify(connection, times(1)).commit(); // Verify that commit was called
        verify(preparedStatement, times(3)).close(); // Ensure all statements were closed
    }

    @Test
    public void testRemovePark_SQLExceptionOnGetOrder() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error on getOrder"));

        Exception exception = assertThrows(SQLException.class, () -> favoriteServiceSpy.removePark("testUser", "park123"));
        assertEquals("Failed to remove park: Database error on getOrder", exception.getMessage());
    }

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

        doReturn(Collections.emptyMap()).when(favoriteServiceSpy).fetchAmenitiesForParks(anyList());

        List<Park> result = favoriteServiceSpy.fetchParkDetailsBatch(0, parkCodes);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("SNP", result.get(0).getParkCode());

        // Verify fetchAmenitiesForParks was called with non-null argument
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        verify(favoriteServiceSpy).fetchAmenitiesForParks(captor.capture());
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
            favoriteServiceSpy.fetchParkDetailsBatch(0, parkCodes);
        });

        assertTrue(exception.getCause() instanceof JsonMappingException);
    }

    @Test
    void testFetchParkDetailsBatch_NotSuccessfulResponse() {
        // Simulate a non-successful HTTP response
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        List<String> parkCodes = List.of("YOSE"); // Example park code
        List<Park> parks = favoriteServiceSpy.fetchParkDetailsBatch(0, parkCodes);

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
        List<Park> parks = favoriteServiceSpy.fetchParkDetailsBatch(0, parkCodes);

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

        List<Park> result = favoriteServiceSpy.fetchParkDetailsBatch(0, parkCodes);

        assertTrue(result.isEmpty(), "Result should be empty when no data is returned from the API.");
    }

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

        Map<String, List<Park.Amenity>> result = favoriteServiceSpy.fetchAmenitiesForParks(parkCodes);

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
            favoriteServiceSpy.fetchAmenitiesForParks(parkCodes);
        });

        assertTrue(exception.getCause() instanceof JsonMappingException);
    }

    @Test
    void fetchAmenitiesForParks_NotSuccessfulResponse() {
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        List<String> parkCodes = Collections.singletonList("PARK123");
        Map<String, List<Park.Amenity>> parks = favoriteServiceSpy.fetchAmenitiesForParks(parkCodes);

        assertTrue(parks.isEmpty(), "Parks list should be empty when the response is not successful.");
    }

    @Test
    void fetchAmenitiesForParks_NullParkSearchResponse() throws JsonProcessingException {
        when(restTemplate.postForEntity(anyString(), Mockito.<HttpEntity<?>>any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        when(objectMapper.readValue(anyString(), eq(AmenityPerParkResponse.class)))
                .thenReturn(null);

        List<String> parkCodes = Collections.singletonList("PARK123");
        Map<String, List<Park.Amenity>> parks = favoriteServiceSpy.fetchAmenitiesForParks(parkCodes);

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

        Map<String, List<Park.Amenity>> parks = favoriteServiceSpy.fetchAmenitiesForParks(parkCodes);

        assertTrue(parks.isEmpty(), "Result should be empty when no data is returned from the API.");
    }

    @Test
    public void testMoveParkDownAtBottom() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(2);
        when(resultSet.getInt("parkOrder")).thenReturn(2);
        favoriteServiceSpy.movePark("user", "parkCode", false);
        verify(connection).rollback(); // Verify rollback is called on fail
    }

    @Test
    public void testMoveParkUpAtTop() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(2);
        when(resultSet.getInt("parkOrder")).thenReturn(1);
        favoriteServiceSpy.movePark("user", "parkCode", true);
        verify(connection).rollback(); // Verify rollback is called on fail
    }

    @Test
    public void testConnectionIsNullInMoveParkCatchBlock() throws SQLException {
        // Simulate connection throwing SQLException
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failure"));

        Exception exception = assertThrows(SQLException.class, () -> {
            favoriteServiceSpy.movePark("user", "parkCode", true);
        });

        // As connection is null, it should not attempt to roll back and throw null pointer exception
        assertNotNull(exception);
        assertEquals("Failed to move park: Connection failure", exception.getMessage());
    }

    @Test
    public void testConnectionIsNullInRemoveParkCatchBlock() throws SQLException {
        // Simulate connection throwing SQLException
        when(dataSource.getConnection()).thenThrow(new SQLException("Connection failure"));

        Exception exception = assertThrows(SQLException.class, () -> {
            favoriteServiceSpy.removePark("user", "parkCode");
        });

        // As connection is null, it should not attempt to roll back and throw null pointer exception
        assertNotNull(exception);
        assertEquals("Failed to remove park: Connection failure", exception.getMessage());
    }

    @Test
    public void getPublic() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean("isPublic")).thenReturn(true);
        boolean result = favoriteServiceSpy.getPublic("user");
        assertTrue(result);
    }

    @Test
    public void getPublic_null() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        boolean result = favoriteServiceSpy.getPublic("user");
        assertFalse(result);
    }

    @Test
    public void getPublic_ShouldThrowSQLException_WhenDatabaseAccessFails() throws SQLException {
        // Arrange
        String expectedMessage = "Failed to get public status: Database access error";
        when(dataSource.getConnection()).thenThrow(new SQLException("Database access error"));

        // Act
        Exception exception = assertThrows(SQLException.class, () -> {
            favoriteServiceSpy.getPublic("user");
        });

        // Assert
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Failed to get public status:"));
    }

    @Test
    void getPublic_ResourceCloseException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean("isPublic")).thenReturn(true);
        doThrow(new SQLException("ResultSet close error")).when(resultSet).close();
        doThrow(new SQLException("preparedStatement close error")).when(preparedStatement).close();
        doThrow(new SQLException("Connection close error")).when(connection).close();

        favoriteServiceSpy.getPublic("user");

        verify(resultSet).close();

        verify(preparedStatement).close();
        verify(connection).close();
    }

}
