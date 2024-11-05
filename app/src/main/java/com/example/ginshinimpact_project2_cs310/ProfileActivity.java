package com.example.ginshinimpact_project2_cs310;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextBio;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextBio = findViewById(R.id.editTextBio);
        buttonSave = findViewById(R.id.buttonSave);

        loadProfile(); // Load existing profile data

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void loadProfile() {
        SharedPreferences prefs = getSharedPreferences("userProfile", MODE_PRIVATE);
        editTextName.setText(prefs.getString("name", ""));
        editTextEmail.setText(prefs.getString("email", ""));
        editTextBio.setText(prefs.getString("bio", ""));
    }

    private void saveProfile() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String bio = editTextBio.getText().toString();

        SharedPreferences prefs = getSharedPreferences("userProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("bio", bio);
        editor.apply();

        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}