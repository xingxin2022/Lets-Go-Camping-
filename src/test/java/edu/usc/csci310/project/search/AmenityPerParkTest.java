package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmenityPerParkTest {
    @Test
    public void testSetAndGetId() {
        AmenityPerPark amenityPark = new AmenityPerPark();
        amenityPark.setId("1");
        assertEquals("1", amenityPark.getId());
    }

    @Test
    public void testSetAndGetName() {
        AmenityPerPark amenityPark = new AmenityPerPark();
        amenityPark.setName("Hiking Trails");
        assertEquals("Hiking Trails", amenityPark.getName());
    }

    @Test
    public void testSetAndGetCategories() {
        AmenityPerPark amenityPark = new AmenityPerPark();
        List<String> categories = new ArrayList<>();
        categories.add("Yosemite National Park");

        amenityPark.setCategories(categories);

        assertNotNull(amenityPark.getCategories());
        assertEquals(1, amenityPark.getCategories().size());
        assertEquals("Yosemite National Park", amenityPark.getCategories().get(0));
    }


}