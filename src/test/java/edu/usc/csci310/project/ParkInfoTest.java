package edu.usc.csci310.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkInfoTest {

    private ParkInfo parkInfo;
    private final String parkName = "Yellowstone";
    private final String parkCode = "YNP";
    private final Boolean isPrivate = true;

    @BeforeEach
    void setUp() {
        parkInfo = new ParkInfo(parkName, parkCode, isPrivate);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(parkName, parkInfo.getParkName(), "Constructor should set and get the correct parkName.");
        assertEquals(parkCode, parkInfo.getParkCode(), "Constructor should set and get the correct parkCode.");
        assertEquals(isPrivate, parkInfo.getPrivate(), "Constructor should set and get the correct isPrivate.");
    }

    @Test
    void testSetParkName() {
        String newParkName = "Grand Canyon";
        parkInfo.setParkName(newParkName);
        assertEquals(newParkName, parkInfo.getParkName(), "setParkName should update the parkName.");
    }

    @Test
    void testSetParkCode() {
        String newParkCode = "GCNP";
        parkInfo.setParkCode(newParkCode);
        assertEquals(newParkCode, parkInfo.getParkCode(), "setParkCode should update the parkCode.");
    }

    @Test
    void testSetPrivate() {
        Boolean newIsPrivate = false;
        parkInfo.setPrivate(newIsPrivate);
        assertEquals(newIsPrivate, parkInfo.getPrivate(), "setPrivate should update the isPrivate flag.");
    }
}