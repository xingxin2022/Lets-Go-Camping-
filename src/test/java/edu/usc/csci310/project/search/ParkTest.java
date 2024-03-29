package edu.usc.csci310.project.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkTest {

    @Test
    public void testSetAndGetId() {
        Park park = new Park();
        park.setId("1");
        assertEquals("1", park.getId());
    }

    @Test
    public void testSetAndGetFullName() {
        Park park = new Park();
        park.setFullName("Yosemite National Park");
        assertEquals("Yosemite National Park", park.getFullName());
    }

    @Test
    public void testSetAndGetUrl() {
        Park park = new Park();
        String testUrl = "https://example.com/yosemite";
        park.setUrl(testUrl);
        assertEquals(testUrl, park.getUrl());
    }

    @Test
    public void testSetAndGetDescription() {
        Park park = new Park();
        String testDescription = "A vast national park offering diverse outdoor recreation.";
        park.setDescription(testDescription);
        assertEquals(testDescription, park.getDescription());
    }

    @Test
    public void testSetAndGetParkCode() {
        Park park = new Park();
        String testParkCode = "YOSE";
        park.setParkCode(testParkCode);
        assertEquals(testParkCode, park.getParkCode());
    }

    @Test
    public void testSetAndGetStates() {
        Park park = new Park();
        String testStates = "CA";
        park.setStates(testStates);
        assertEquals(testStates, park.getStates());
    }

    @Test
    public void testSetAndGetActivities() {
        Park park = new Park();
        List<Park.Activity> activities = new ArrayList<>();
        Park.Activity activity = new Park.Activity();
        activity.setId("A1");
        activity.setName("Hiking");
        activities.add(activity);
        park.setActivities(activities);

        assertEquals(1, park.getActivities().size());
        assertEquals("A1", park.getActivities().get(0).getId());
        assertEquals("Hiking", park.getActivities().get(0).getName());
    }

    @Test
    public void testSetAndGetActivityParks() {
        Park park = new Park();

        List<Park.DetailedActivityPark> activityParks = new ArrayList<>();

        Park.DetailedActivityPark activityPark1 = new Park.DetailedActivityPark();
        activityPark1.setParkCode("YOSE");
        activityPark1.setStates("CA");
        activityPark1.setDesignation("National Park");
        activityPark1.setFullName("Yosemite National Park");
        activityPark1.setUrl("https://example.com/yosemite");
        activityPark1.setName("Yosemite");


        Park.DetailedActivityPark activityPark2 = new Park.DetailedActivityPark();
        activityPark2.setParkCode("YELL");
        activityPark2.setStates("WY");
        activityPark2.setDesignation("National Park");
        activityPark2.setFullName("Yellowstone National Park");
        activityPark2.setUrl("https://example.com/yellowstone");
        activityPark2.setName("Yellowstone");


        activityParks.add(activityPark1);
        activityParks.add(activityPark2);


        park.setActivityParks(activityParks);


        List<Park.DetailedActivityPark> retrievedActivityParks = park.getActivityParks();
        assertNotNull(retrievedActivityParks, "Activity parks list should not be null");
        assertEquals(2, retrievedActivityParks.size(), "Activity parks list size should be 2");


        Park.DetailedActivityPark retrievedActivityPark1 = retrievedActivityParks.get(0);
        assertEquals("YOSE", retrievedActivityPark1.getParkCode());
        assertEquals("CA", retrievedActivityPark1.getStates());
        assertEquals("National Park", retrievedActivityPark1.getDesignation());
        assertEquals("Yosemite National Park", retrievedActivityPark1.getFullName());
        assertEquals("https://example.com/yosemite", retrievedActivityPark1.getUrl());
        assertEquals("Yosemite", retrievedActivityPark1.getName());


        Park.DetailedActivityPark retrievedActivityPark2 = retrievedActivityParks.get(1);
        assertEquals("YELL", retrievedActivityPark2.getParkCode());
        assertEquals("WY", retrievedActivityPark2.getStates());
        assertEquals("National Park", retrievedActivityPark2.getDesignation());
        assertEquals("Yellowstone National Park", retrievedActivityPark2.getFullName());
        assertEquals("https://example.com/yellowstone", retrievedActivityPark2.getUrl());
        assertEquals("Yellowstone", retrievedActivityPark2.getName());
    }


    @Test
    public void testSetAndGetImages() {
        Park park = new Park();
        List<Park.Image> images = Arrays.asList(new Park.Image());
        park.setImages(images);
        assertEquals(1, park.getImages().size());
    }

    @Test
    public void testSetAndImages() {
        Park park = new Park();
        Park.Image image = new Park.Image();
        image.setCredit("testCredit");
        image.setTitle("testTitle");
        image.setAltText("testAltText");
        image.setCaption("testCaption");
        image.setUrl("testUrl");


        assertEquals("testCredit", image.getCredit());
        assertEquals("testTitle", image.getTitle());
        assertEquals("testAltText", image.getAltText());
        assertEquals("testCaption", image.getCaption());
        assertEquals("testUrl", image.getUrl());

    }

    @Test
    public void testSetAndGetEntranceFees() {
        Park park = new Park();
        List<Park.EntranceFee> entranceFees = new ArrayList<>();
        Park.EntranceFee fee = new Park.EntranceFee();
        fee.setCost("25.00");
        fee.setDescription("Per vehicle fee");
        fee.setTitle("Standard Entry");
        entranceFees.add(fee);

        park.setEntranceFees(entranceFees);

        assertEquals(1, park.getEntranceFees().size());
        Park.EntranceFee retrievedFee = park.getEntranceFees().get(0);
        assertEquals("25.00", retrievedFee.getCost());
        assertEquals("Per vehicle fee", retrievedFee.getDescription());
        assertEquals("Standard Entry", retrievedFee.getTitle());
    }

    @Test
    public void testSetAndGetAddresses() {
        Park park = new Park();

        List<Park.Address> addresses = new ArrayList<>();

        Park.Address address1 = new Park.Address();
        address1.setLine1("123 Yosemite Park Road");
        address1.setCity("Yosemite National Park");
        address1.setStateCode("CA");
        address1.setPostalCode("95389");
        address1.setCountryCode("US");
        address1.setProvinceTerritoryCode("S1");
        address1.setType("National");
        address1.setLine2("2");
        address1.setLine3("3");


        Park.Address address2 = new Park.Address();
        address2.setLine1("456 Yellowstone Park Road");
        address2.setCity("Yellowstone National Park");
        address2.setStateCode("WY");
        address2.setPostalCode("82190");
        address2.setCountryCode("US");
        address2.setProvinceTerritoryCode("S2");
        address2.setType("National");
        address2.setLine2("2");
        address2.setLine3("3");

        addresses.add(address1);
        addresses.add(address2);

        park.setAddresses(addresses);

        List<Park.Address> retrievedAddresses = park.getAddresses();
        assertNotNull(retrievedAddresses, "Addresses list should not be null");
        assertEquals(2, retrievedAddresses.size(), "Addresses list size should be 2");

        Park.Address retrievedAddress1 = retrievedAddresses.get(0);
        assertEquals("123 Yosemite Park Road", retrievedAddress1.getLine1());
        assertEquals("Yosemite National Park", retrievedAddress1.getCity());
        assertEquals("CA", retrievedAddress1.getStateCode());
        assertEquals("95389", retrievedAddress1.getPostalCode());
        assertEquals("US", retrievedAddress1.getCountryCode());
        assertEquals("S1", retrievedAddress1.getProvinceTerritoryCode());
        assertEquals("National", retrievedAddress1.getType());
        assertEquals("2", retrievedAddress1.getLine2());
        assertEquals("3", retrievedAddress1.getLine3());

        Park.Address retrievedAddress2 = retrievedAddresses.get(1);
        assertEquals("456 Yellowstone Park Road", retrievedAddress2.getLine1());
        assertEquals("Yellowstone National Park", retrievedAddress2.getCity());
        assertEquals("WY", retrievedAddress2.getStateCode());
        assertEquals("82190", retrievedAddress2.getPostalCode());
        assertEquals("US", retrievedAddress2.getCountryCode());
        assertEquals("S2", retrievedAddress2.getProvinceTerritoryCode());
        assertEquals("National", retrievedAddress2.getType());
        assertEquals("2", retrievedAddress2.getLine2());
        assertEquals("3", retrievedAddress2.getLine3());
    }
}

