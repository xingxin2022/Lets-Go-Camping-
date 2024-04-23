package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;

import java.util.List;

public class FavoriteResponse {
    private List<Park> parks;
    private String message;
    private boolean success;

    public FavoriteResponse() {
    }

    public FavoriteResponse(List<Park> parks, String message, boolean success) {
        this.parks = parks;
        this.message = message;
        this.success = success;
    }

    public List<Park> getParks() {
        return parks;
    }

    public void setParks(List<Park> parks) {
        this.parks = parks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
