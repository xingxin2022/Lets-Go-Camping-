package edu.usc.csci310.project;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    public void testUser() {
        User user = new User("test", "test");
        assert(user.getUsername().equals("test"));
        assert(user.getPassword().equals("test"));
    }

    @Test
    public void testUserSetUsername() {
        User user = new User("test", "test");
        user.setUsername("test2");
        assert(user.getUsername().equals("test2"));
    }

    @Test
    public void testUserSetPassword() {
        User user = new User("test", "test");
        user.setPassword("test2");
        assert(user.getPassword().equals("test2"));
    }
}
