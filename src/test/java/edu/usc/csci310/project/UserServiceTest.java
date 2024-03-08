package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;

import static org.mockito.Mockito.*;
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
    public void registerUserAlreadyExists() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        User user = new User("testUser", "Password123");

        RegisterResponse response = userService.registerUser(user, "Password123");

        assertEquals("Username already exists", response.getMessage());
    }

    @Test
    public void registerUserSuccess() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");
        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    public void registerUserSuccess2() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(0);
        when(resultSet.next()).thenReturn(true);

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");
        assertEquals("User registered successfully", response.getMessage());
    }

    @Test
    public void registerPasswordMismatch() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password2b");

        assertEquals("Passwords do not match", response.getMessage());
    }

    @Test
    public void registerInvalidPassword() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        User user = new User("newUser", "password1");
        RegisterResponse response = userService.registerUser(user, "password1");
        assertEquals("Password must contain at least one uppercase letter", response.getMessage());

        user = new User("newUser", "PASSWORD1");
        response = userService.registerUser(user, "PASSWORD1");
        assertEquals("Password must contain at least one lowercase letter", response.getMessage());

        user = new User("newUser", "Password");
        response = userService.registerUser(user, "Password");
        assertEquals("Password must contain at least one digit", response.getMessage());

    }

    @Test
    public void registerUserSQLException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection error"));

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");

        assertEquals("Error when registering user: Database connection error", response.getMessage());
    }

    @Test
    public void registerUserSQLException2() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(dataSource.getConnection()).thenThrow(new SQLTimeoutException("Database connection error"));

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");

        assertEquals("Error when registering user: Database connection error", response.getMessage());
    }

    @Test
    public void registerUserSQLException3() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("SQL Error"));

        User user = new User("newUser", "Password1a");
        RegisterResponse response = userService.registerUser(user, "Password1a");

        assertEquals("Error when registering user: SQL Error", response.getMessage());
    }


    @Test
    public void loginUserSuccess() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("Password123");

        LoginResponse response = userService.loginUser("testUser", "Password123");

        assertEquals("Login Successful", response.getMessage());
    }

    @Test
    public void loginUserFailure() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("password")).thenReturn("Password123");

        LoginResponse response = userService.loginUser("testUser", "WrongPassword");

        assertEquals("Password is incorrect", response.getMessage());
    }

    @Test
    public void loginUserUsernameDoesNotExist() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        LoginResponse response = userService.loginUser("nonExistentUser", "Password123");

        assertEquals("Username does not exist", response.getMessage());
    }

    @Test
    public void loginUserSQLException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection error"));

        LoginResponse response = userService.loginUser("testUser", "Password123");

        assertEquals("Error when trying to login: Database connection error", response.getMessage());
    }

    @Test
    public void loginUserSQLException2() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("SQL Error"));

        LoginResponse response = userService.loginUser("testUser", "Password123");

        assertEquals("Error when trying to login: SQL Error", response.getMessage());
    }

    @Test
    public void loginUserSQLException3() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenThrow(new SQLException("SQL Error"));

        LoginResponse response = userService.loginUser("testUser", "Password123");

        assertEquals("Error when trying to login: SQL Error", response.getMessage());
    }

    @Test
    public void initializeDatabase() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);

        userService.initializeDatabase();

        verify(statement, times(1)).execute(anyString());
    }

    @Test
    public void initializeDatabaseSQLException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        doThrow(new SQLException("Error executing SQL")).when(statement).execute(anyString());

        userService.initializeDatabase();
    }


}