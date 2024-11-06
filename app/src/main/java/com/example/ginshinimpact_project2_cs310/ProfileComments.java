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

public class ProfileComments extends AppCompatActivity {

    private LinearLayout linearLayoutUserComments;
    private DatabaseReference usersRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comments);

        // Initialize layout where comments will be added
        linearLayoutUserComments = findViewById(R.id.linearLayoutUserComments);

        // Retrieve user ID from UserSession
        currentUserID = UserSession.getInstance().getUserProfile().ID;

        // Reference to the "users" node in Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load the current user's comments
        loadUserComments();
    }

    private void loadUserComments() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutUserComments.removeAllViews(); // Clear existing views

                boolean userFound = false;

                // Loop through all users to find the one with the matching ID
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.child("ID").getValue(String.class);

                    if (userID != null && userID.equals(currentUserID)) {
                        userFound = true;
                        DataSnapshot commentsSnapshot = userSnapshot.child("comments");

                        // Iterate over all comments for the matched user
                        for (DataSnapshot commentSnapshot : commentsSnapshot.getChildren()) {
                            String username = commentSnapshot.child("username").getValue(String.class);
                            String content = commentSnapshot.child("content").getValue(String.class);
                            String rating = commentSnapshot.child("rating").getValue(String.class);

                            if (username != null && content != null && rating != null) {
                                addCommentToLayout(username, content, rating);
                            }
                        }
                        break; // Exit loop once the user is found
                    }
                }

                if (!userFound) {
                    Toast.makeText(ProfileComments.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileComments.this, "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCommentToLayout(String username, String content, String rating) {
        TextView usernameTextView = new TextView(this);
        usernameTextView.setText("Username: " + username);
        usernameTextView.setTextSize(16);
        usernameTextView.setPadding(0, 16, 0, 4);

        TextView ratingTextView = new TextView(this);
        ratingTextView.setText("Rating: " + rating);
        ratingTextView.setTextSize(14);
        ratingTextView.setPadding(0, 4, 0, 4);

        TextView contentTextView = new TextView(this);
        contentTextView.setText(content);
        contentTextView.setTextSize(14);
        contentTextView.setPadding(0, 4, 0, 16);

        linearLayoutUserComments.addView(usernameTextView);
        linearLayoutUserComments.addView(ratingTextView);
        linearLayoutUserComments.addView(contentTextView);
    }
}
