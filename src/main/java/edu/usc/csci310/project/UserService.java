package edu.usc.csci310.project;

import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class UserService {
    private final DataSource dataSource;

    @Autowired
    public UserService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RegisterResponse registerUser(User user, String confirmPassword) throws UserAlreadyExistsException, InvalidPasswordException {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {

            checkUserStmt.setString(1, user.getUsername());
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new UserAlreadyExistsException("Username already exists");
            }

            // Validate password
            validatePassword(user.getPassword(), confirmPassword);

            // Insert user
            try (PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSql)) {
                insertUserStmt.setString(1, user.getUsername());
                insertUserStmt.setString(2, user.getPassword());
                insertUserStmt.executeUpdate();
            }
            return new RegisterResponse("User registered successfully");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            // Consider a more generic exception for SQL errors if needed
            throw new RuntimeException("Error when registering user: " + sqle.getMessage());
        }
    }

    public void validatePassword(String password, String confirmPassword) throws InvalidPasswordException {
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        // Check for password requirements
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new InvalidPasswordException("Password must contain at least one digit");
        }
    }

    public LoginResponse loginUser(String username, String password) throws LoginFailedException {
        String checkUserSql = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(password)) {
                    throw new LoginFailedException("Invalid username or password");
                }
                return new LoginResponse("Login Successful");
            } else {
                throw new LoginFailedException("Username does not exist");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            // Consider a more generic exception for SQL errors if needed
            throw new RuntimeException("Error when trying to login: " + sqle.getMessage());
        }
    }
    @PostConstruct
    public void initializeDatabase() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

}
