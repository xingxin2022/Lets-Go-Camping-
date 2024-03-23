package edu.usc.csci310.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public LoginResponse loginUser(@RequestParam String username,
                                   @RequestParam String password)
    {
        return userService.loginUser(username, password);
    }
}
