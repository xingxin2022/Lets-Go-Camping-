package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTest {
    @Test
    public void testHashFunction() {
        String input = "user";
        String expected = "04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb";
        String hashed = Hash.hash(input);
        assertNotNull(hashed);
        assertEquals(hashed, expected);
    }
}
