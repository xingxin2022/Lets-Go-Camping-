package edu.usc.csci310.project;

import edu.usc.csci310.project.exceptions.InvalidPasswordException;
import edu.usc.csci310.project.exceptions.LoginFailedException;
import edu.usc.csci310.project.exceptions.UserAlreadyExistsException;
import edu.usc.csci310.project.search.Park;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam String confirmPassword) {
        try {
            User user = new User(username, password);
            RegisterResponse response = userService.registerUser(user, confirmPassword);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(HttpSession session,
                                       @RequestParam String username,
                                       @RequestParam String password) {
        try {
            LoginResponse response = userService.loginUser(username, password);
            if ("Login Successful".equals(response.getMessage())) {
                session.setAttribute("username", username);
            }

            return ResponseEntity.ok(response);
        } catch (LoginFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/compare")
    public ResponseEntity<?> compareUser(@RequestParam List<String> usernames) {

        List<Map.Entry<Park, Double>> sortedParks = userService.getFavoriteParksSortedByPopularity(usernames);
        List<ParkPopularity> results = sortedParks.stream()
                .map(entry -> new ParkPopularity(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(HttpSession session) {
        String user = (String) session.getAttribute("username");
        return user != null ? ResponseEntity.ok(user) :
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
}

