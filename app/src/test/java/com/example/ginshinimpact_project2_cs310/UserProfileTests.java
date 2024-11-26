package com.example.ginshinimpact_project2_cs310;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UserProfileTests {
    @Test
    public void SetPasswordTest() {
        UserProfile testProfile = new UserProfile("testUsername", "testEmail@usc.edu", "testID");
        testProfile.setPassword("testPassword");
        assertEquals("testPassword", testProfile.getPassword());

        testProfile.setPassword("testPassword2");
        assertEquals("testPassword2", testProfile.getPassword());
    }

    @Test
    public void SetEmailTest() {
        UserProfile testProfile = new UserProfile("testUsername", "testEmail@usc.edu", "testID");
        testProfile.setEmail("testEmail2@usc.edu");
        assertEquals("testEmail2@usc.edu", testProfile.getEmail());

        testProfile.setEmail("testEmail3@usc.edu");
        assertEquals("testEmail3@usc.edu", testProfile.getEmail());
    }

    @Test
    public void SetUsernameTest() {
        UserProfile testProfile = new UserProfile("testUsername", "testEmail", "testID");
        testProfile.setUsername("username123");
        assertEquals("username123", testProfile.getUsername());
    }

    @Test
    public void SetIdTest() {
        UserProfile testProfile = new UserProfile("testUsername", "testEmail", "testID");
        testProfile.setID("idTest");
        assertEquals("idTest", testProfile.getID());
    }
}
