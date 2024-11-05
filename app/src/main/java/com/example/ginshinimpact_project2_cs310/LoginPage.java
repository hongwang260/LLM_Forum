package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        databaseRef = FirebaseDatabase.getInstance().getReference();

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

        // Set up the click listener for the Login button
        buttonLogin.setOnClickListener(v -> {
            String emailOrId = editTextEmail.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            if (emailOrId.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(LoginPage.this, "Please enter both email/ID and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            String lookupKey = isValidEmail(emailOrId) ? customEncodeEmail(emailOrId) : emailOrId;
            Log.d(TAG, "Encoded key for lookup: " + lookupKey); // Debugging log for encoded key
            verifyUserCredentials(lookupKey, passwordInput);
        });
    }

    // Method to check if the input is a valid email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    Log.d(TAG, "stored password: " + storedPassword);
                    if (storedPassword != null && storedPassword.equals(passwordInput)) {
                        // Password matches, proceed to ProfileActivity
                        Intent intent = new Intent(LoginPage.this, ProfileActivity.class);
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
