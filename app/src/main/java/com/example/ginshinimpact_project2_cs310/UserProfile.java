package com.example.ginshinimpact_project2_cs310;

import java.util.Map;

public class UserProfile {
    public String username;
    public String email;
    public String ID;
    private String password;
    //map that stores PostId to post object type
    public Map<String, Post> comments;

    //ALL Comments made, along with Post ID to clear. NO repetitive ID

    public UserProfile() {
        username = "";
        email = "";
        password = "";

    }

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
