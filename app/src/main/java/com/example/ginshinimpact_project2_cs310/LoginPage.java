package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private DatabaseReference databaseRef;
    private static final String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Set up padding for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find input fields and buttons
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignup = findViewById(R.id.buttonSignup);

        // Set up the click listener for the Login button
        buttonLogin.setOnClickListener(v -> {
            String emailOrId = editTextEmail.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            if (emailOrId.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please enter both email/ID and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            String lookupKey = customEncodeEmail(emailOrId);
            verifyUserCredentials(lookupKey, passwordInput);
        });

        // Set up the click listener for the signup button to redirect the user to signup page
        buttonSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, SignupPage.class);
            startActivity(intent);
        });
    }

    // Method to URL-encode the email address for Firebase key usage
    // Custom method to encode email to a Firebase-compatible key
    private String customEncodeEmail(String email) {
        return email.replace("@", "%40").replace(".", "%2E");
    }

    // Method to verify credentials by looking up in Firebase
    private void verifyUserCredentials(String key, String passwordInput) {
        databaseRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //data that is stored in the database, used to keep track of session
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    String storedUsername = dataSnapshot.child("username").getValue(String.class);
                    String storedID = dataSnapshot.child("ID").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    if (storedPassword != null && storedPassword.equals(passwordInput)) {
                        UserProfile userProfile = new UserProfile(storedUsername, email, storedID);
                        userProfile.setPassword(storedPassword);
                        Log.d("user ID in login", "user Id is : " + userProfile.ID);
                        UserSession.getInstance().setUserProfile(userProfile);

                        // Password matches, proceed to ProfileActivity and pass the encoded email key
                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                        intent.putExtra("encodedEmailKey", key);  // Pass the encoded email key
                        intent.putExtra("email", email);           // Pass the actual email for display
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginPage.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPage.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginPage.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
