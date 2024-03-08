package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;
public class RegisterResponseTest {
    @Test
    public void testRegisterResponse() {
        RegisterResponse registerResponse = new RegisterResponse("test");
        assert(registerResponse.getMessage().equals("test"));
    }

    @Test
    public void testRegisterResponseSetMessage() {
        RegisterResponse registerResponse = new RegisterResponse("test");
        registerResponse.setMessage("test2");
        assert(registerResponse.getMessage().equals("test2"));
    }

    @Test
    public void testRegisterResponseToString() {
        RegisterResponse registerResponse = new RegisterResponse("test");
        assert(registerResponse.toString().equals("RegisterResponse{message='test'}"));
    }
}
