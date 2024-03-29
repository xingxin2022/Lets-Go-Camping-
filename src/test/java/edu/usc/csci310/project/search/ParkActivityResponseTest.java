package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ParkActivityResponseTest {
    @Test
    public void testSetAndGetData() {
        ParkActivityResponse response = new ParkActivityResponse();

        List<ActivityPark> activityParks = new ArrayList<>();

        ActivityPark activityPark1 = new ActivityPark();
        activityPark1.setId("1");
        activityPark1.setName("Camping");
        activityParks.add(activityPark1);

        ActivityPark activityPark2 = new ActivityPark();
        activityPark2.setId("2");
        activityPark2.setName("Hiking");
        activityParks.add(activityPark2);

        response.setData(activityParks);

        List<ActivityPark> retrievedActivityParks = response.getData();
        assertNotNull(retrievedActivityParks);
        assertEquals(2, retrievedActivityParks.size());

        ActivityPark retrievedActivityPark1 = retrievedActivityParks.get(0);
        assertEquals("1", retrievedActivityPark1.getId());
        assertEquals("Camping", retrievedActivityPark1.getName());

        ActivityPark retrievedActivityPark2 = retrievedActivityParks.get(1);
        assertEquals("2", retrievedActivityPark2.getId());
        assertEquals("Hiking", retrievedActivityPark2.getName());
    }
}
