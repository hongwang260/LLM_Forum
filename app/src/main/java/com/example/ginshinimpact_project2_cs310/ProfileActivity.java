package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail;
    private Button buttonSave;

    private DatabaseReference mDatabase;
    private String encodedEmailKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve the encoded email key and email from the intent
        encodedEmailKey = getIntent().getStringExtra("encodedEmailKey");
        String email = getIntent().getStringExtra("email");

        if (email != null) {
            editTextEmail.setText(email);  // Auto-fill email
        }

        if (encodedEmailKey != null) {
            loadUserProfile(encodedEmailKey);
        }

        buttonSave.setOnClickListener(v -> checkUsernameExistsAndSave());
    }

    private void loadUserProfile(String encodedKey) {
        // Fetch user profile data from Firebase
        mDatabase.child("users").child(encodedKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);
                if (profile != null) {
                    editTextUsername.setText(profile.username);
                    editTextEmail.setText(profile.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsernameExistsAndSave() {
        String username = editTextUsername.getText().toString();

        // Check if username already exists in the database
        mDatabase.child("users").orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Username already exists, show an error message
                            Toast.makeText(ProfileActivity.this, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, proceed to update the profile
                            updateUserProfile(username);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, "Error checking username", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserProfile(String username) {
        String email = editTextEmail.getText().toString();
        // Only update the username and email fields
        if (encodedEmailKey != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("username", username);
            updates.put("email", email);

            // Use updateChildren to only update specific fields without overwriting others
            mDatabase.child("users").child(encodedEmailKey).updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}