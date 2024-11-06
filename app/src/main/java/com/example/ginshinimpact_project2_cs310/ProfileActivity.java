package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
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
    private Button buttonSave;

    private DatabaseReference mDatabase;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve UserProfile from UserSession
        userProfile = UserSession.getInstance().getUserProfile();

        // Check if UserProfile is not null
        if (userProfile != null) {
            // Load user data into the EditText fields
            loadUserProfile();
        } else {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // End this activity if UserProfile is null
        }

        buttonSave.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserProfile() {
        // Display user profile data in EditText fields
        editTextUsername.setText(userProfile.username);
        editTextEmail.setText(userProfile.email);
    }

    private void updateUserProfile() {
        // Get updated data from EditText fields
        String updatedUsername = editTextUsername.getText().toString().trim();
        String updatedEmail = editTextEmail.getText().toString().trim();

        // Only update the fields that changed
        Map<String, Object> updates = new HashMap<>();
        if (!updatedUsername.equals(userProfile.username)) {
            updates.put("username", updatedUsername);
        }
        if (!updatedEmail.equals(userProfile.email)) {
            updates.put("email", updatedEmail);
        }

        // Save changes to Firebase if there are any updates
        if (!updates.isEmpty() && userProfile.ID != null) {
            mDatabase.child("users").child(userProfile.ID).updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                            // Update the UserProfile in UserSession
                            userProfile.username = updatedUsername;
                            userProfile.email = updatedEmail;
                            UserSession.getInstance().setUserProfile(userProfile);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No changes to update", Toast.LENGTH_SHORT).show();
        }
    }
}