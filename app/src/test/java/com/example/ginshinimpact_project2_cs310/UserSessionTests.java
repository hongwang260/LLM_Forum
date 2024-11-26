package com.example.ginshinimpact_project2_cs310;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserSessionTests {

    @Test
    public void setUserSessionTest() {
        UserProfile profile = new UserProfile("testUser", "testUser@usc.edu", "testUserID");
        UserSession testingSession = new UserSession();
        testingSession.setUserProfile(profile);

        UserProfile gotSession = testingSession.getUserProfile();
        assertEquals("testUser", gotSession.username);
        assertEquals("testUser@usc.edu", gotSession.email);
        assertEquals("testUserID", gotSession.ID);
    }

    @Test
    public void clearSessionTest() {
        UserProfile profile = new UserProfile("testUser", "testUser@usc.edu", "testUserID");
        UserSession testingSession = new UserSession();
        testingSession.setUserProfile(profile);
        testingSession.clearSession();

        UserProfile gotSession = testingSession.getUserProfile();
        assertEquals(null, gotSession);
    }
}
