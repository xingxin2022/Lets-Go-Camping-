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
        Connection connection = null;
        PreparedStatement checkUserStmt = null;
        PreparedStatement insertUserStmt = null;
        ResultSet rs = null;

        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try {
            connection = dataSource.getConnection();
            checkUserStmt = connection.prepareStatement(checkUserSql);

            checkUserStmt.setString(1, user.getUsername());
            rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new RegisterResponse("Username already exists");
            }

            // Validate password
            RegisterResponse response = validatePassword(user.getPassword(), confirmPassword);
            if (!response.getMessage().equals("Password is valid")) {
                return response;
            }

            // Insert user
            insertUserStmt = connection.prepareStatement(insertUserSql);
            insertUserStmt.setString(1, user.getUsername());
            insertUserStmt.setString(2, user.getPassword());
            insertUserStmt.executeUpdate();

        } catch (SQLTimeoutException sqlte) {
            sqlte.printStackTrace();
            return new RegisterResponse("Error when registering user: " + sqlte.getMessage());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new RegisterResponse("Error when registering user: " + sqle.getMessage());
        } finally {
            // close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new RegisterResponse("Error when registering user: " + e.getMessage());
                }
            }
            if (insertUserStmt != null) {
                try {
                    insertUserStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new RegisterResponse("Error when registering user: " + e.getMessage());
                }
            }
            if (checkUserStmt != null) {
                try {
                    checkUserStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new RegisterResponse("Error when registering user: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new RegisterResponse("Error when registering user: " + e.getMessage());
                }
            }
        }
        return new RegisterResponse("User registered successfully");
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
        Connection connection = null;
        PreparedStatement checkUserStmt = null;
        ResultSet rs = null;

        String checkUserSql = "SELECT password FROM users WHERE username = ?";

        try {
            connection = dataSource.getConnection();
            checkUserStmt = connection.prepareStatement(checkUserSql);
            checkUserStmt.setString(1, username);
            rs = checkUserStmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (!storedPassword.equals(password)) {
                    return new LoginResponse("Password is incorrect");
                }
            } else {
                return new LoginResponse("Username does not exist");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return new LoginResponse("Error when trying to login: " + sqle.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new LoginResponse("Error when trying to login: " + e.getMessage());
                }
            }
            if (checkUserStmt != null) {
                try {
                    checkUserStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new LoginResponse("Error when trying to login: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new LoginResponse("Error when trying to login: " + e.getMessage());
                }
            }
        }
        return new LoginResponse("Login Successful");
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
