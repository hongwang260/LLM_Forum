package com.example.ginshinimpact_project2_cs310;

// used to keep track of logged in user
public class UserSession {
    private static UserSession instance;
    private UserProfile userProfile;

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void clearSession() {
        this.userProfile = null;
    }
}

