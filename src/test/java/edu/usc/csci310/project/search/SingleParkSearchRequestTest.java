package edu.usc.csci310.project.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingleParkSearchRequestTest {

    private SingleParkSearchRequest request;

    @BeforeEach
    void setUp() {
        request = new SingleParkSearchRequest();
    }

    @Test
    void getParkCode_InitiallyNull() {
        assertNull(request.getParkCode(), "parkCode should initially be null.");
    }

    @Test
    void setParkCode_SetsValueCorrectly() {
        String parkCode = "YNP"; // Example park code
        request.setParkCode(parkCode);
        assertEquals(parkCode, request.getParkCode(), "setParkCode should set the parkCode correctly.");
    }

    @Test
    void setParkCode_NullValue() {
        request.setParkCode(null);
        assertNull(request.getParkCode(), "setParkCode with null should set parkCode to null.");
    }
}