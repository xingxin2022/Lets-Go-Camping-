package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ParkAmenityResponseTest {
    @Test
    public void testSetAndGetData() {
        ParkAmenityResponse response = new ParkAmenityResponse();

        List<List<AmenityPark>> data = new ArrayList<>();

        List<AmenityPark> amenityParks1 = new ArrayList<>();
        AmenityPark amenityPark1 = new AmenityPark();
        amenityPark1.setId("1");
        amenityPark1.setName("Camping");
        amenityParks1.add(amenityPark1);

        List<AmenityPark> amenityParks2 = new ArrayList<>();
        AmenityPark amenityPark2 = new AmenityPark();
        amenityPark2.setId("2");
        amenityPark2.setName("Hiking");
        amenityParks2.add(amenityPark2);

        data.add(amenityParks1);
        data.add(amenityParks2);

        response.setData(data);

        List<List<AmenityPark>> retrievedData = response.getData();
        assertNotNull(retrievedData);
        assertEquals(2, retrievedData.size());

        assertEquals(1, retrievedData.get(0).size());
        AmenityPark retrievedAmenityPark1 = retrievedData.get(0).get(0);
        assertEquals("1", retrievedAmenityPark1.getId());
        assertEquals("Camping", retrievedAmenityPark1.getName());

        assertEquals(1, retrievedData.get(1).size());
        AmenityPark retrievedAmenityPark2 = retrievedData.get(1).get(0);
        assertEquals("2", retrievedAmenityPark2.getId());
        assertEquals("Hiking", retrievedAmenityPark2.getName());
    }
}
