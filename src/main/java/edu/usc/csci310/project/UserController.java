package edu.usc.csci310.project;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam String confirmPassword)
    {
        User user = new User(username, password);
        return userService.registerUser(user, confirmPassword);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(HttpSession session,
                                   @RequestParam String username,
                                   @RequestParam String password)
    {
        LoginResponse response = userService.loginUser(username, password);
        if ("Login Successful".equals(response.getMessage())) {
            session.setAttribute("username", username);
        }
        return response;
    }

    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username != null ? ResponseEntity.ok(username) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "Logged out successfully";
    }

}
