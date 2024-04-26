package edu.usc.csci310.project;


import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
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
            checkUserStmt.setString(1, Hash.hash(user.getUsername()));
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new UserAlreadyExistsException("Username already exists");
            }


            // Validate password
            validatePassword(user.getPassword(), confirmPassword);


            // Insert user
            try (PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSql)) {
                insertUserStmt.setString(1, Hash.hash(user.getUsername()));
                insertUserStmt.setString(2, Hash.hash(user.getPassword()));
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
            checkUserStmt.setString(1, Hash.hash(username));
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(Hash.hash(password))) {
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
        String dropTableSql = "DROP TABLE IF EXISTS users";
        String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Drop the existing table
            stmt.execute(dropTableSql);
            // Recreate the table
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT DISTINCT username FROM favorites";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all usernames", e);
        }

        return usernames;
    }


    public List<ParkInfo> getFavoriteParksByUsername(String username) {
        List<ParkInfo> favoriteParks = new ArrayList<>();
        String sql = "SELECT parkName, parkCode, isPublic FROM favorites WHERE username = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String parkName = resultSet.getString("parkName");
                String parkCode = resultSet.getString("parkCode");
                Boolean isPublic = resultSet.getBoolean("isPublic");
                favoriteParks.add(new ParkInfo(parkName, parkCode, !isPublic));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving favorite parks", e);
        }


        return favoriteParks;
    }


    public List<ParkCount> getUnionFavoriteParks(List<String> usernames) throws Exception {
        Map<String, ParkCount> parkCountMap = new HashMap<>();
        if (usernames.size() < 2) {
            throw new Exception("Need to select 2 or more users for comparison.");
        }

        for (String username : usernames) {
            List<ParkInfo> favoriteParks = getFavoriteParksByUsername(username);
            if (favoriteParks.stream().anyMatch(ParkInfo::getPrivate)) {
                throw new Exception("Comparing failed: the user '" + username + "' has a private list.");
            }

            for (ParkInfo parkInfo : favoriteParks) {
                String parkKey = parkInfo.getParkCode();
                parkCountMap.computeIfAbsent(parkKey, k -> new ParkCount(parkInfo.getParkName(), parkInfo.getParkCode()))
                        .incrementCount(username);
            }
        }

        int totalUsers = usernames.size();
        parkCountMap.values().forEach(parkCount -> parkCount.calculateRatio(totalUsers));

        return parkCountMap.values().stream()
                .sorted(Comparator.comparing(ParkCount::getRatio).reversed())
                .collect(Collectors.toList());
    }
}

