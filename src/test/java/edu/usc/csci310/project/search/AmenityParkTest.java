package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmenityParkTest {
    @Test
    public void testSetAndGetId() {
        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setId("1");
        assertEquals("1", amenityPark.getId());
    }

    @Test
    public void testSetAndGetName() {
        AmenityPark amenityPark = new AmenityPark();
        amenityPark.setName("Hiking Trails");
        assertEquals("Hiking Trails", amenityPark.getName());
    }

    @Test
    public void testSetAndGetAmenityParks() {
        AmenityPark amenityPark = new AmenityPark();
        List<AmenityPark.DetailedAmenityPark> detailedAmenityParks = new ArrayList<>();

        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        detailedAmenityPark.setFullName("Yosemite National Park");
        detailedAmenityParks.add(detailedAmenityPark);

        amenityPark.setAmenityParks(detailedAmenityParks);

        assertNotNull(amenityPark.getAmenityParks());
        assertEquals(1, amenityPark.getAmenityParks().size());
        assertEquals("Yosemite National Park", amenityPark.getAmenityParks().get(0).getFullName());
    }

    @Test
    public void testSetAndDetailedAmenityPark() {
        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        detailedAmenityPark.setFullName("Yosemite National Park");
        detailedAmenityPark.setParkCode("YOSE");
        detailedAmenityPark.setStates("CA");
        detailedAmenityPark.setDesignation("National Park");
        detailedAmenityPark.setUrl("https://nps.gov/yose");
        detailedAmenityPark.setName("Yosemite");


        assertEquals("Yosemite National Park", detailedAmenityPark.getFullName());
        assertEquals("YOSE", detailedAmenityPark.getParkCode());
        assertEquals("CA", detailedAmenityPark.getStates());
        assertEquals("National Park", detailedAmenityPark.getDesignation());
        assertEquals("https://nps.gov/yose", detailedAmenityPark.getUrl());
        assertEquals("Yosemite", detailedAmenityPark.getName());
    }

    @Test
    public void testSetAndGetPlaces() {
        AmenityPark.DetailedAmenityPark detailedAmenityPark = new AmenityPark.DetailedAmenityPark();
        List<AmenityPark.DetailedAmenityPark.Place> places = new ArrayList<>();

        AmenityPark.DetailedAmenityPark.Place place = new AmenityPark.DetailedAmenityPark.Place();
        place.setId("1");
        place.setTitle("Half Dome");
        place.setUrl("https://nps.gov/yose/places/halfdome");
        places.add(place);

        detailedAmenityPark.setPlaces(places);

        assertNotNull(detailedAmenityPark.getPlaces());
        assertEquals(1, detailedAmenityPark.getPlaces().size());
        assertEquals("1", detailedAmenityPark.getPlaces().get(0).getId());
        assertEquals("Half Dome", detailedAmenityPark.getPlaces().get(0).getTitle());
        assertEquals("https://nps.gov/yose/places/halfdome", detailedAmenityPark.getPlaces().get(0).getUrl());
    }

}