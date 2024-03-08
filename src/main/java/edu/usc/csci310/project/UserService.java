package edu.usc.csci310.project;

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

    public RegisterResponse registerUser(User user, String confirmPassword) {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {

            checkUserStmt.setString(1, user.getUsername());
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new RegisterResponse("Username already exists");
            }

            // Validate password
            RegisterResponse response = validatePassword(user.getPassword(), confirmPassword);
            if (!response.getMessage().equals("Password is valid")) {
                return response;
            }

            // Insert user
            PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSql);
            insertUserStmt.setString(1, user.getUsername());
            insertUserStmt.setString(2, user.getPassword());
            insertUserStmt.executeUpdate();
            return new RegisterResponse("User registered successfully");
        } catch (SQLTimeoutException sqlte) {
            sqlte.printStackTrace();
            return new RegisterResponse("Error when registering user: " + sqlte.getMessage());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new RegisterResponse("Error when registering user: " + sqle.getMessage());
        }
    }

    public RegisterResponse validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return new RegisterResponse("Passwords do not match");
        }

        // Check for password requirements
        if (!password.matches(".*[A-Z].*")) {
            return new RegisterResponse("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            return new RegisterResponse("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            return new RegisterResponse("Password must contain at least one digit");
        }
        return new RegisterResponse("Password is valid");
    }

    public LoginResponse loginUser(String username, String password) {
        String checkUserSql = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql)) {
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    return new LoginResponse("Login Successful");
                } else {
                    return new LoginResponse("Password is incorrect");
                }
            } else {
                return new LoginResponse("Username does not exist");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new LoginResponse("Error when trying to login: " + sqle.getMessage());
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