package edu.usc.csci310.project;

public class RegisterResponse {
    private String message;

    public RegisterResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // used for debugging
    @Override
    public String toString() {
        return "RegisterResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
