package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityParkTest {
    @Test
    public void testSetAndGetName() {
        ActivityPark activityPark = new ActivityPark();
        activityPark.setName("Hiking");
        assertEquals("Hiking", activityPark.getName());
    }

    @Test
    public void testSetAndGetId() {
        ActivityPark activityPark = new ActivityPark();
        activityPark.setId("123");
        assertEquals("123", activityPark.getId());
    }

    @Test
    public void testSetAndGetActivityParks() {
        ActivityPark activityPark = new ActivityPark();
        List<ActivityPark.DetailedActivityPark> detailedActivityParks = new ArrayList<>();

        ActivityPark.DetailedActivityPark detailedPark = new ActivityPark.DetailedActivityPark();
        detailedPark.setFullName("Yosemite National Park");
        detailedPark.setParkCode("YOSE");
        detailedPark.setStates("CA");
        detailedPark.setDesignation("National Park");
        detailedPark.setUrl("https://nps.gov/yose");
        detailedActivityParks.add(detailedPark);

        activityPark.setActivityParks(detailedActivityParks);

        assertNotNull(activityPark.getActivityParks());
        assertEquals(1, activityPark.getActivityParks().size());
        ActivityPark.DetailedActivityPark retrievedPark = activityPark.getActivityParks().get(0);
        assertEquals("Yosemite National Park", retrievedPark.getFullName());
        assertEquals("YOSE", retrievedPark.getParkCode());
        assertEquals("CA", retrievedPark.getStates());
        assertEquals("National Park", retrievedPark.getDesignation());
        assertEquals("https://nps.gov/yose", retrievedPark.getUrl());
    }

    @Test
    public void testSetAndGetDetailedActivityPark() {
        ActivityPark.DetailedActivityPark detailedPark = new ActivityPark.DetailedActivityPark();
        detailedPark.setFullName("Yellowstone National Park");
        detailedPark.setParkCode("YELL");
        detailedPark.setStates("WY,MT,ID");
        detailedPark.setDesignation("National Park");
        detailedPark.setUrl("https://nps.gov/yell");
        detailedPark.setName("Yellowstone");

        assertEquals("Yellowstone National Park", detailedPark.getFullName());
        assertEquals("YELL", detailedPark.getParkCode());
        assertEquals("WY,MT,ID", detailedPark.getStates());
        assertEquals("National Park", detailedPark.getDesignation());
        assertEquals("https://nps.gov/yell", detailedPark.getUrl());
        assertEquals("Yellowstone", detailedPark.getName());
    }
}
