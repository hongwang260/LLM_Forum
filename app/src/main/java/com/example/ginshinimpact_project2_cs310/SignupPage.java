package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupPage extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize the EditText and Button
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonSignup = findViewById(R.id.buttonSignup);

        // Set up the click listener for the Signup button
        buttonSignup.setOnClickListener(v -> {
            String emailOrId = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            // Check if all fields are filled in
            if (emailOrId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupPage.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email or ID
            if (!isValidEmailOrId(emailOrId)) {
                Toast.makeText(SignupPage.this, "Invalid Email or ID. Please use an '@usc.edu' email or a 10-digit ID.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupPage.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the user to Firebase and proceed to ProfileActivity
            saveUserToFirebase(emailOrId, password);
        });
    }

    // Method to validate if the input is a valid USC email or a 10-digit ID
    private boolean isValidEmailOrId(String input) {
        // Pattern for a 10-digit ID
        Pattern idPattern = Pattern.compile("^\\d{10}$");

        if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            // Check if it's a USC email
            return input.endsWith("@usc.edu");
        } else {
            // Check if it's a 10-digit ID
            return idPattern.matcher(input).matches();
        }
    }

    // Method to save the user data to Firebase
    private void saveUserToFirebase(String emailOrId, String password) {
        // Encode the email if it's an email to make it Firebase-compatible as a key
        String key = emailOrId.contains("@") ? encodeEmail(emailOrId) : emailOrId;
        //use a unique id foe each user that exist in the database
        DatabaseReference newUserRef = databaseRef.push();
        String uniqueId = newUserRef.getKey();

        // Create user profile to pass the stored values into Firebase
        UserProfile user = new UserProfile();
        user.email = emailOrId;
        user.username = "";
        user.ID = uniqueId;
        user.setPassword(password);
        UserSession.getInstance().setUserProfile(user);

        // Save to Firebase under the 'users' node
        databaseRef.child(key).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupPage.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to ProfileActivity
                        Intent intent = new Intent(SignupPage.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupPage.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to encode email to a Firebase-compatible key
    private String encodeEmail(String email) {
        return email.replace("@", "%40").replace(".", "%2E");
    }
}
