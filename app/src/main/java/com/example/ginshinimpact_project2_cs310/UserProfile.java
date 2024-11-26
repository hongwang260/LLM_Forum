package com.example.ginshinimpact_project2_cs310;

import java.util.Map;

public class UserProfile {
    private String username;
    private String email;
    private String ID;
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

    public void setUsername(String username) {this.username = username;}
    public String getUsername () {return this.username;}

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return this.email;}

    public void setID(String id) {this.ID = id;}
    public String getID() {return this.ID;}
}
