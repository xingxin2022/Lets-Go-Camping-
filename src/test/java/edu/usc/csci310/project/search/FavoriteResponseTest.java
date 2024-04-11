package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteResponseTest {
    @Test
    public void testSetAndGetMessage() {
        FavoriteResponse favoriteResponse = new FavoriteResponse("test");
        favoriteResponse.setMessage("test message");
        assertEquals("test message", favoriteResponse.getMessage());
    }

    @Test
    public void testToString() {
        FavoriteResponse favoriteResponse = new FavoriteResponse("test");
        favoriteResponse.toString();
        assertEquals("LoginResponse{message='test'}",favoriteResponse.toString());
    }
}