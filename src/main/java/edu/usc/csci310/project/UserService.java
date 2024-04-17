package edu.usc.csci310.project;

import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;

import edu.usc.csci310.project.search.Park;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            throw new InvalidPasswordException("Please fill out both password fields");
        }
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

    // Dummy implementation of a method to get favorite parks by username
    public List<Park> getFavoriteParksByUsername(String username) {
        List<Park> favoriteParks = new ArrayList<>();
        String sql = "SELECT parkCode, parkName FROM favorites WHERE username = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String parkCode = rs.getString("parkCode");
                String parkName = rs.getString("parkName");
                Park parkToAdd = new Park();
                parkToAdd.setParkCode(parkCode);
                parkToAdd.setFullName(parkName);
                favoriteParks.add(parkToAdd);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing database", e);
        }
        return favoriteParks;
    }
    public List<Map.Entry<Park, Double>> getFavoriteParksSortedByPopularity(List<String> usernames) {
        Map<Park, Integer> parkCount = new HashMap<>();
        try {
            for (String username : usernames) {
                List<Park> favorites = getFavoriteParksByUsername(username);
                for (Park park : favorites) {
                    parkCount.merge(park, 1, Integer::sum);
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }

        final int totalUsers = usernames.size();
        return parkCount.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (entry.getValue() * 100.0) / totalUsers))
                .sorted(Map.Entry.<Park, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
    }
}
