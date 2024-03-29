package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ParkSearchResponseTest {
    @Test
    public void testSetAndGetData() {
        ParkSearchResponse response = new ParkSearchResponse();

        List<Park> parks = new ArrayList<>();

        Park park1 = new Park();
        park1.setId("1");
        park1.setFullName("Yosemite National Park");
        parks.add(park1);

        Park park2 = new Park();
        park2.setId("2");
        park2.setFullName("Yellowstone National Park");
        parks.add(park2);

        response.setData(parks);

        List<Park> retrievedParks = response.getData();
        assertNotNull(retrievedParks);
        assertEquals(2, retrievedParks.size());


        assertEquals("1", retrievedParks.get(0).getId());
        assertEquals("Yosemite National Park", retrievedParks.get(0).getFullName());

        assertEquals("2", retrievedParks.get(1).getId());
        assertEquals("Yellowstone National Park", retrievedParks.get(1).getFullName());
    }

}
