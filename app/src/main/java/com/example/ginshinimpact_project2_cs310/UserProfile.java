package com.example.ginshinimpact_project2_cs310;

import java.util.Map;

public class UserProfile {
    public String username;
    public String email;
    public String ID;
    private String password;

    public UserProfile() {
        username = "";
        email = "";
        password = "";
        ID = "";
    }

    public UserProfile(String username, String email, String ID) {
        this.username = username;
        this.email = email;
        this.ID = ID;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {return this.password;}
}
