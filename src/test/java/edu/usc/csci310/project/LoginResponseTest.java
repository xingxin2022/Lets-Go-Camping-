package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;

public class LoginResponseTest {
    @Test
    public void testLoginResponse() {
        LoginResponse loginResponse = new LoginResponse("test");
        assert(loginResponse.getMessage().equals("test"));
    }

    @Test
    public void testLoginResponseSetMessage() {
        LoginResponse loginResponse = new LoginResponse("test");
        loginResponse.setMessage("test2");
        assert(loginResponse.getMessage().equals("test2"));
    }

    @Test
    public void testLoginResponseToString() {
        LoginResponse loginResponse = new LoginResponse("test");
        assert(loginResponse.toString().equals("LoginResponse{message='test'}"));
    }
}
