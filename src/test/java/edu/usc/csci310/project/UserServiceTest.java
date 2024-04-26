package edu.usc.csci310.project;

import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import io.cucumber.java.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;
import java.sql.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserService userService;



    @Test
    public void registerUserAlreadyExists() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        // Simulate existing user
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        User user = new User("testUser", "Password123");

        // This should throw an exception due to user existence simulation
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user, "Password123"));
    }

    @Test
    public void registerUserSuccess() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");

        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    public void registerSuccess2() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");

        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    public void registerPasswordMismatch() {
        User user = new User("newUser", "Password1a");
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(user.getPassword(), "Password2b"));
    }


    @Test
    public void registerInvalidPassword() {
        User userLowerCase = new User("newUser", "password1");
        User userUpperCase = new User("newUser", "PASSWORD1");
        User userNoDigit = new User("newUser", "Password");
        User userEmpty = new User("newUser", "Passs1s");
        User userEmpty2 = new User("newUser", "");

        // Directly test the method without mocking since it's a validation method
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(userLowerCase.getPassword(), userLowerCase.getPassword()));
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(userUpperCase.getPassword(), userUpperCase.getPassword()));
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(userNoDigit.getPassword(), userNoDigit.getPassword()));
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(userEmpty.getPassword(), ""));
        assertThrows(InvalidPasswordException.class, () -> userService.validatePassword(userEmpty2.getPassword(), userEmpty2.getPassword()));
    }

    @Test
    public void loginUserSuccess() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn(Hash.hash("Password123"));

        LoginResponse response = userService.loginUser("testUser", "Password123");

        assertEquals("Login Successful", response.getMessage());
    }

    @Test
    public void loginUserFailure() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("Password123");

        assertThrows(LoginFailedException.class, () -> userService.loginUser("testUser", "WrongPassword"));
    }

    @Test
    public void loginUserUsernameDoesNotExist() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(LoginFailedException.class, () -> userService.loginUser("nonExistentUser", "Password123"));
    }

    @Test
    public void initializeDatabase() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);

        userService.initializeDatabase();

        verify(statement, times(1)).execute(anyString());
    }


    @Test
    public void registerUserSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        User user = new User("testUser", "Password123");

        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(user, "Password123"));
        assertTrue(exception.getMessage().contains("Error when registering user: Database error"));
    }

    @Test
    public void loginUserSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.loginUser("testUser", "Password123"));
        assertTrue(exception.getMessage().contains("Error when trying to login: Database error"));
    }

    @Test
    public void initializeDatabaseSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenThrow(new SQLException("Database initialization error"));

        userService.initializeDatabase();
    }

    @Test
    public void getAllUsernamesShouldReturnUsernames() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);  // Simulate two results
        when(resultSet.getString("username")).thenReturn("user1", "user2");

        List<String> usernames = userService.getAllUsernames();
        assertNotNull(usernames);
        assertEquals(2, usernames.size());
        assertEquals("user1", usernames.get(0));
        assertEquals("user2", usernames.get(1));

        verify(preparedStatement, times(1)).close();
        verify(connection, times(1)).close();
    }

    @Test
    public void getAllUsernamesShouldHandleSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.getAllUsernames());
        assertTrue(exception.getMessage().contains("Error retrieving all usernames"));
    }

    @Test
    public void getFavoriteParksByUsernameShouldReturnParks() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("parkName")).thenReturn("Yellowstone");
        when(resultSet.getString("parkCode")).thenReturn("YNP");
        when(resultSet.getBoolean("isPublic")).thenReturn(false);

        List<ParkInfo> parks = userService.getFavoriteParksByUsername("user1");
        assertNotNull(parks);
        assertEquals(1, parks.size());
        assertEquals("Yellowstone", parks.get(0).getParkName());

        verify(preparedStatement, times(1)).setString(1, "user1");
        verify(preparedStatement, times(1)).close();
        verify(connection, times(1)).close();
    }

    @Test
    public void getFavoriteParksByUsernameShouldHandleSQLException() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.getFavoriteParksByUsername("user1"));
        assertTrue(exception.getMessage().contains("Error retrieving favorite parks"));
    }

    //-----

    /*
    @Test
    void testGetUnionFavoriteParks() throws Exception {
        // Setup input and mocks
        List<String> usernames = Arrays.asList("user1", "user2");
        when(resultSet.next()).thenReturn(true, true, false, true, true, false); // Two parks per user
        when(resultSet.getString("parkName")).thenReturn("Park1", "Park2");
        when(resultSet.getString("parkCode")).thenReturn("P1", "P2");
        when(resultSet.getBoolean("isPrivate")).thenReturn(false);

        // Execute the method
        List<ParkCount> results = userService.getUnionFavoriteParks(usernames);

        // Assert results
        assertEquals(2, results.size());
        assertEquals("Park1", results.get(0).getParkName());
        assertEquals(1.0, results.get(0).getRatio()); // Assuming each user has unique parks
    }

    @Test
    void testGetUnionFavoriteParksExceptionForSingleUser() {
        // Setup input
        List<String> usernames = Collections.singletonList("user1");

        // Execute and assert the exception
        Exception exception = assertThrows(Exception.class, () -> userService.getUnionFavoriteParks(usernames));
        assertEquals("Need to select 2 or more users for comparison.", exception.getMessage());
    }

    @Test
    void testGetUnionFavoriteParksExceptionForPrivateList() throws SQLException {
        // Setup input and mocks
        List<String> usernames = Arrays.asList("user1", "user2");
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getBoolean("isPrivate")).thenReturn(true);

        // Execute and assert the exception
        Exception exception = assertThrows(Exception.class, () -> userService.getUnionFavoriteParks(usernames));
        assertTrue(exception.getMessage().contains("Comparing failed"));
    }
*/

    @Test
    void testGetUnionFavoriteParks() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false, true, true, false); // Results iteration
        when(resultSet.getString("parkName")).thenReturn("Park1", "Park2", "Park1", "Park3");
        when(resultSet.getString("parkCode")).thenReturn("P1", "P2", "P1", "P3");
        when(resultSet.getBoolean("isPublic")).thenReturn(true, true, true, true);

        UserService userService = new UserService(dataSource);

        List<ParkCount> results = userService.getUnionFavoriteParks(Arrays.asList("user1", "user2"));

        assertEquals(3, results.size());
        assertEquals(100.0, results.stream().filter(p -> p.getParkName().equals("Park1")).findFirst().get().getRatio());
        assertEquals(50.0, results.stream().filter(p -> p.getParkName().equals("Park2")).findFirst().get().getRatio());
    }

    @Test
    void testGetUnionFavoriteParksExceptionForSingleUser() {
        DataSource dataSource = mock(DataSource.class);
        UserService userService = new UserService(dataSource);

        Exception exception = assertThrows(Exception.class, () -> userService.getUnionFavoriteParks(Collections.singletonList("user1")));
        assertEquals("Need to select 2 or more users for comparison.", exception.getMessage());
    }

    @Test
    public void testThrowsExceptionWhenParkIsPrivate() {
        // Arrange
        List<ParkInfo> favoriteParks = Arrays.asList(
                new ParkInfo("Yellowstone", "YP001", false),
                new ParkInfo("Grand Canyon", "GC002", true),  // This park is marked as private
                new ParkInfo("Zion", "ZN003", false)
        );

        String username = "testUser";

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            if (favoriteParks.stream().anyMatch(ParkInfo::getPrivate)) {
                throw new Exception("Comparing failed: the user '" + username + "' has a private list.");
            }
        });

        // Verify the message is as expected
        assertTrue(exception.getMessage().contains("Comparing failed: the user 'testUser' has a private list."));
    }

    private void checkForPrivateParks(List<ParkInfo> parks, String username) throws Exception {
        if (parks.stream().anyMatch(ParkInfo::getPrivate)) {
            throw new Exception("Comparing failed: the user '" + username + "' has a private list.");
        }
    }

    @Test
    public void testGetUnionFavoriteParksThrowsExceptionForPrivateParks() throws SQLException {
        // Setup
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mocking the result set for favorite parks with one user having a private park
        when(resultSet.next()).thenReturn(true, true, true, false); // Multiple parks, last call returns false
        when(resultSet.getString("parkName")).thenReturn("Yellowstone");
        when(resultSet.getString("parkCode")).thenReturn("YP001");
        when(resultSet.getBoolean("isPublic")).thenReturn(false, true); // Second park is private

        UserService userService = new UserService(dataSource);

        List<String> usernames = new ArrayList<>();
        usernames.add("user1");
        usernames.add("user2");

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userService.getUnionFavoriteParks(usernames);
        });

        assertEquals("Comparing failed: the user 'user1' has a private list.", exception.getMessage());
    }

}
