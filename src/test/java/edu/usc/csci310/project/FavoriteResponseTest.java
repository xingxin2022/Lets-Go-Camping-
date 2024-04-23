package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResponseTest {

    @Test
    public void testEmptyConstructor() {
        FavoriteResponse response = new FavoriteResponse();
        assertNull(response.getParks());
        assertNull(response.getMessage());
        assertFalse(response.isSuccess());
    }

    @Test
    public void testConstructorWithParams() {
        List<Park> parks = new ArrayList<>();
        parks.add(new Park());

        FavoriteResponse response = new FavoriteResponse(parks, "Success", true);

        assertEquals(parks, response.getParks());
        assertEquals("Success", response.getMessage());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testSettersAndGetters() {
        FavoriteResponse response = new FavoriteResponse();
        List<Park> parks = new ArrayList<>();
        parks.add(new Park());

        response.setParks(parks);
        response.setMessage("Updated Success");
        response.setSuccess(true);

        assertEquals(parks, response.getParks());
        assertEquals("Updated Success", response.getMessage());
        assertTrue(response.isSuccess());
    }
}
