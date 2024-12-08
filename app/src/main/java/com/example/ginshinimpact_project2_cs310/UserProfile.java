package com.example.ginshinimpact_project2_cs310;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserProfile {
    private String username;
    private String email;
    private String ID;
    private String password;
    private String jobTitle;     // New field
    private String gender;       // New field
    private String selfIntro;    // New field

    public UserProfile() {
        username = "";
        email = "";
        password = "";
        ID = "";
        jobTitle = "";
        gender = "";
        selfIntro = "";
    }

    public UserProfile(String username, String email, String ID) {
        this.username = username;
        this.email = email;
        this.ID = ID;
        this.jobTitle = "";
        this.gender = "";
        this.selfIntro = "";
        loadUserAttributesById(ID, this);
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

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getJobTitle() { return this.jobTitle; }

    public void setGender(String gender) { this.gender = gender; }
    public String getGender() { return this.gender; }

    public void setSelfIntro(String selfIntro) { this.selfIntro = selfIntro; }
    public String getSelfIntro() { return this.selfIntro; }

    private void loadUserAttributesById(String userId, UserProfile userProfile) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("ID").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve the new fields from the database
                        String jobTitle = userSnapshot.child("jobTitle").getValue(String.class);
                        String gender = userSnapshot.child("gender").getValue(String.class);
                        String selfIntro = userSnapshot.child("selfIntro").getValue(String.class);

                        // Update the UserProfile object
                        userProfile.setJobTitle(jobTitle != null ? jobTitle : "");
                        userProfile.setGender(gender != null ? gender : "");
                        userProfile.setSelfIntro(selfIntro != null ? selfIntro : "");

                        // Debug or success log
                        Log.d("UserProfile", "Loaded attributes: " +
                                "Job Title: " + userProfile.getJobTitle() +
                                ", Gender: " + userProfile.getGender() +
                                ", Self Intro: " + userProfile.getSelfIntro());
                    }
                } else {
                    // No user found with the given ID
                    Log.e("UserProfile", "No user found with ID: " + userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Log.e("UserProfile", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
