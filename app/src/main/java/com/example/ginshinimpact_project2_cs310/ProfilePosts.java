package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePosts extends AppCompatActivity {

    private LinearLayout linearLayoutUserPosts;
    private DatabaseReference usersRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_posts);

        // Initialize layout where posts will be added
        linearLayoutUserPosts = findViewById(R.id.linearLayoutUserPosts);

        // Retrieve user ID from UserSession
        currentUserID = UserSession.getInstance().getUserProfile().ID;

        // Reference to the "users" node in Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load the current user's posts
        loadUserPosts();
    }

    private void loadUserPosts() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutUserPosts.removeAllViews(); // Clear existing views

                boolean userFound = false;

                // Loop through all users to find the one with the matching ID
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.child("ID").getValue(String.class);

                    if (userID != null && userID.equals(currentUserID)) {
                        userFound = true;
                        DataSnapshot postsSnapshot = userSnapshot.child("posts");

                        // Iterate over all posts for the matched user
                        for (DataSnapshot postSnapshot : postsSnapshot.getChildren()) {
                            String title = postSnapshot.child("title").getValue(String.class);
                            String llmKind = postSnapshot.child("llmKind").getValue(String.class);

                            if (title != null && llmKind != null) {
                                addPostToLayout(title, llmKind);
                            }
                        }
                        break; // Exit loop once the user is found
                    }
                }

                if (!userFound) {
                    Toast.makeText(ProfilePosts.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfilePosts.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToLayout(String title, String llmKind) {
        TextView titleTextView = new TextView(this);
        titleTextView.setText("Title: " + title);
        titleTextView.setTextSize(18);
        titleTextView.setPadding(0, 16, 0, 4);

        TextView modelTextView = new TextView(this);
        modelTextView.setText("Model: " + llmKind);
        modelTextView.setTextSize(16);
        modelTextView.setPadding(0, 4, 0, 16);

        linearLayoutUserPosts.addView(titleTextView);
        linearLayoutUserPosts.addView(modelTextView);
    }
}
