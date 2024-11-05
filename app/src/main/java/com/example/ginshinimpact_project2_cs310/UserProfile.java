package com.example.ginshinimpact_project2_cs310;

public class UserProfile {
    public String username;
    public String email;
    private String password;
    //Array for Posts

    //ALL Comments made, along with Post ID to clear. NO repetitive ID

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    }

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
