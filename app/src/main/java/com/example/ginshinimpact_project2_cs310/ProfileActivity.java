package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail;
    private Button buttonSave, buttonLogout;

    private DatabaseReference mDatabase;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve UserProfile from UserSession
        userProfile = UserSession.getInstance().getUserProfile();

        // Check if UserProfile exists in session (i.e., user is logged in)
        if (userProfile != null && userProfile.email != null) {
            // Load user data into the EditText fields
            loadUserProfile();
        } else {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // End this activity if UserProfile is null or email is missing
        }

        buttonSave.setOnClickListener(v -> updateUserProfile());
        buttonLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserProfile() {
        // Display user profile data in EditText fields
        editTextUsername.setText(userProfile.username);
        editTextEmail.setText(userProfile.email);
    }

    private void updateUserProfile() {
        String updatedUsername = editTextUsername.getText().toString().trim();

        Map<String, Object> updates = new HashMap<>();
        if (!updatedUsername.equals(userProfile.username)) {
            updates.put("username", updatedUsername);
        }

        if (!updates.isEmpty() && userProfile.email != null) {
            String encodedEmail = userProfile.email.replace(".", "%2E").replace("@", "%40");
            mDatabase.child(encodedEmail).updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            userProfile.username = updatedUsername;
                            UserSession.getInstance().setUserProfile(userProfile);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        // Clear the user session
        UserSession.getInstance().clearSession();

        // Verify that session data is cleared with Log.d statements
        if (UserSession.getInstance().getUserProfile() == null) {
            Log.d("ProfileActivity", "User session cleared successfully.");
        } else {
            Log.d("ProfileActivity", "User session not cleared properly.");
        }

        // Inform the user and redirect to login screen
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity or MainActivity
        Intent intent = new Intent(ProfileActivity.this, LoginPage.class); // Replace with your login activity
        startActivity(intent);
        finish(); // Close the profile activity
    }
}