package edu.usc.csci310.project;

import edu.usc.csci310.project.search.Park;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class ParkPopularityTest {

    private Park park;
    private ParkPopularity parkPopularity;
    private final double popularity = 8.5;

    @BeforeEach
    void setUp() {
        // Mock the Park object
        park = Mockito.mock(Park.class);
        Mockito.when(park.getFullName()).thenReturn("Yellowstone National Park");

        // Initialize ParkPopularity with the mocked Park and a sample popularity score
        parkPopularity = new ParkPopularity(park, popularity);
    }

    @Test
    void testConstructorAndGetterFullName() {
        assertEquals("Yellowstone National Park", parkPopularity.getFullName(), "The fullName should match the Park's fullName.");
    }

    @Test
    void testGetterPopularity() {
        assertEquals(popularity, parkPopularity.getPopularity(), "The popularity should match the one set in the constructor.");
    }
}