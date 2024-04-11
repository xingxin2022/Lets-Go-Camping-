package edu.usc.csci310.project.search;

public class FavoriteResponse {

    private String message;

    public FavoriteResponse(String message) {
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
        return "LoginResponse{" +
                "message='" + message + '\'' +
                '}';
    }

}
