package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
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

    @Override
    protected void onResume() {
        super.onResume();
        // Reload comments when the activity resumes to reflect any updates
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
                            String postID = commentSnapshot.child("postId").getValue(String.class);
                            String commentID = commentSnapshot.getKey();

                            Object ratingObj = commentSnapshot.child("rating").getValue();
                            String rating = null;

                            if (ratingObj instanceof Long) {
                                rating = String.valueOf(ratingObj); // Convert Long to String
                            } else if (ratingObj instanceof String) {
                                rating = (String) ratingObj; // Use String directly
                            }

                            if (username != null && content != null && rating != null) {
                                addCommentToLayout(commentID, username, content, rating, postID);
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

    private void addCommentToLayout(String commentId, String username, String content, String rating, String postID) {
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

        // Wrap these TextViews in a LinearLayout to make the entire comment clickable
        LinearLayout commentLayout = new LinearLayout(this);
        commentLayout.setOrientation(LinearLayout.VERTICAL);
        commentLayout.setPadding(0, 16, 0, 16);
        commentLayout.addView(usernameTextView);
        commentLayout.addView(ratingTextView);
        commentLayout.addView(contentTextView);

        // Set an OnClickListener to open CommentModifier for editing
        commentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileComments.this, CommentModifier.class);
            intent.putExtra("commentId", commentId);
            intent.putExtra("postId", postID);  // Assuming postId is available in your comment structure
            intent.putExtra("username", username);
            intent.putExtra("content", content);
            intent.putExtra("rating", rating);
            intent.putExtra("isEdit", true);  // Use this flag to indicate editing mode
            startActivity(intent);
        });

        // Add the comment layout to the main layout
        linearLayoutUserComments.addView(commentLayout);
    }
}
