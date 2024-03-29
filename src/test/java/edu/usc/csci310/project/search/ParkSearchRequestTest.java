package edu.usc.csci310.project.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParkSearchRequestTest {
    @Test
    public void testSetAndGetQuery() {
        ParkSearchRequest request = new ParkSearchRequest();
        String testQuery = "Yosemite";
        request.setQuery(testQuery);
        assertEquals(testQuery, request.getQuery());
    }

    @Test
    public void testSetAndGetSearchType() {
        ParkSearchRequest request = new ParkSearchRequest();
        String testSearchType = "National Park";
        request.setSearchType(testSearchType);
        assertEquals(testSearchType, request.getSearchType());
    }

    @Test
    public void testSetAndGetStartPosition() {
        ParkSearchRequest request = new ParkSearchRequest();
        int testStartPosition = 10;
        request.setStartPosition(testStartPosition);
        assertEquals(testStartPosition, request.getStartPosition());
    }
}
