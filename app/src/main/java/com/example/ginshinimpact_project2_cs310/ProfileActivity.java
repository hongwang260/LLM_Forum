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

    private EditText editTextUsername, editTextEmail, editTextID;
    // 新增的字段 EditText
    private EditText editTextJobTitle, editTextGender, editTextSelfIntro;
    private Button buttonSave, buttonLogout;

    private DatabaseReference mDatabase;
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextID = findViewById(R.id.editTextID);

        editTextJobTitle = findViewById(R.id.editTextJobTitle);
        editTextGender = findViewById(R.id.editTextGender);
        editTextSelfIntro = findViewById(R.id.editTextSelfIntro);

        buttonSave = findViewById(R.id.buttonSave);
        buttonLogout = findViewById(R.id.buttonLogout);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        userProfile = UserSession.getInstance().getUserProfile();

        // check for logged in user
        if (userProfile != null && userProfile.getEmail() != null) {
            // Load user data into the EditText fields
            loadUserProfile();
        } else {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonSave.setOnClickListener(v -> updateUserProfile());
        buttonLogout.setOnClickListener(v -> logoutUser());
    }

    // Display user profile data in EditText fields
    private void loadUserProfile() {
        editTextUsername.setText(userProfile.getUsername());
        editTextEmail.setText(userProfile.getEmail());
        editTextID.setText(userProfile.getID());

        // 新增字段的显示
        editTextJobTitle.setText(userProfile.getJobTitle());
        editTextGender.setText(userProfile.getGender());
        editTextSelfIntro.setText(userProfile.getSelfIntro());
    }

    private void updateUserProfile() {
        String updatedUsername = editTextUsername.getText().toString().trim();
        String updatedJobTitle = editTextJobTitle.getText().toString().trim();
        String updatedGender = editTextGender.getText().toString().trim();
        String updatedSelfIntro = editTextSelfIntro.getText().toString().trim();

        Map<String, Object> updates = new HashMap<>();
        // 检查是否有修改
        if (!updatedUsername.equals(userProfile.getUsername())) {
            updates.put("username", updatedUsername);
        }
        if (!updatedJobTitle.equals(userProfile.getJobTitle())) {
            updates.put("jobTitle", updatedJobTitle);
        }
        if (!updatedGender.equals(userProfile.getGender())) {
            updates.put("gender", updatedGender);
        }
        if (!updatedSelfIntro.equals(userProfile.getSelfIntro())) {
            updates.put("selfIntro", updatedSelfIntro);
        }

        if (!updates.isEmpty() && userProfile.getEmail() != null) {
            String encodedEmail = userProfile.getEmail().replace(".", "%2E").replace("@", "%40");
            mDatabase.child(encodedEmail).updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            // 更新本地 userProfile
                            userProfile.setUsername(updatedUsername);
                            userProfile.setJobTitle(updatedJobTitle);
                            userProfile.setGender(updatedGender);
                            userProfile.setSelfIntro(updatedSelfIntro);

                            UserSession.getInstance().setUserProfile(userProfile);
                            Intent intent = new Intent(ProfileActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
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

        // Inform the user and redirect to login screen
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to Login page
        Intent intent = new Intent(ProfileActivity.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}