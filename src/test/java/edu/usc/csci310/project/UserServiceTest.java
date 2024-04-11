package edu.usc.csci310.project;

import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

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
        when(resultSet.getString("password")).thenReturn("Password123");

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

}
