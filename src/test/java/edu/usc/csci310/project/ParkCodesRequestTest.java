package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

class ParkCodesRequestTest {

    @Mock
    private ParkCodesRequest parkCodesRequest;

    @Test
    @DisplayName("Test ParkCodesRequest constructor")
    void testParkCodesRequestConstructor() {
        parkCodesRequest = new ParkCodesRequest();
        assertTrue(parkCodesRequest != null);
    }

    @Test
    @DisplayName("Test ParkCodesRequest getParkCodes")
    void testParkCodesRequestGetParkCodes() {
        parkCodesRequest = new ParkCodesRequest();
        List<String> parkCodes = Arrays.asList("YELL", "GRTE");
        parkCodesRequest.setParkCodes(parkCodes);
        assertEquals(parkCodes, parkCodesRequest.getParkCodes());
    }

    @Test
    @DisplayName("Test ParkCodesRequest setParkCodes")
    void testParkCodesRequestSetParkCodes() {
        parkCodesRequest = new ParkCodesRequest();
        List<String> parkCodes = Arrays.asList("YELL", "GRTE");
        parkCodesRequest.setParkCodes(parkCodes);
        assertEquals(parkCodes, parkCodesRequest.getParkCodes());
    }
}
