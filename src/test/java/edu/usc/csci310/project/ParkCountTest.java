package edu.usc.csci310.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParkCountTest {

    private ParkCount parkCount;
    private final String parkName = "Yellowstone";
    private final String parkCode = "YNP";

    @BeforeEach
    void setUp() {
        parkCount = new ParkCount(parkName, parkCode);
    }

    @Test
    void testConstructor() {
        assertEquals(parkName, parkCount.getParkName(), "Park name should match the constructor input.");
        assertEquals(parkCode, parkCount.getParkCode(), "Park code should match the constructor input.");
        assertEquals(0, parkCount.getRatio(), "Initial ratio should be 0.");
        assertTrue(parkCount.getUsernames().isEmpty(), "Initial list of usernames should be empty.");
    }

    @Test
    void testIncrementCount() {
        String username = "user1";
        parkCount.incrementCount(username);
        assertEquals(1, parkCount.getUsernames().size(), "Username list should contain one entry.");
        assertTrue(parkCount.getUsernames().contains(username), "Usernames should include 'user1'.");
        assertEquals(1, parkCount.getUsernames().size(), "Count should be incremented to 1.");
    }

    @Test
    void testCalculateRatio() {
        parkCount.incrementCount("user1");
        parkCount.incrementCount("user2");
        parkCount.calculateRatio(10);
        assertEquals(20.0f, parkCount.getRatio(), "Ratio should be calculated as 20.0.");
    }

    @Test
    void testSetParkName() {
        String newName = "Grand Canyon";
        parkCount.setParkName(newName);
        assertEquals(newName, parkCount.getParkName(), "Setter for parkName should update the name.");
    }

    @Test
    void testSetParkCode() {
        String newCode = "GCNP";
        parkCount.setParkCode(newCode);
        assertEquals(newCode, parkCount.getParkCode(), "Setter for parkCode should update the code.");
    }

    @Test
    void testSetRatio() {
        float newRatio = 50.0f;
        parkCount.setRatio(newRatio);
        assertEquals(newRatio, parkCount.getRatio(), "Setter for ratio should update the ratio.");
    }
}
