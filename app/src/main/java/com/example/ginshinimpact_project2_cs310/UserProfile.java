package com.example.ginshinimpact_project2_cs310;

public class UserProfile {
    public String username;
    public String email;

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    }

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
