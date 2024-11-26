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

        linearLayoutUserComments = findViewById(R.id.linearLayoutUserComments);
        currentUserID = UserSession.getInstance().getUserProfile().getID();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load the current user's comments
        loadUserComments();
    }

    // use to load all the comments for the logged in user
    private void loadUserComments() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutUserComments.removeAllViews(); // Clear existing views

                // Loop through all users to find the one with the matching ID
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.child("ID").getValue(String.class);

                    if (userID != null && userID.equals(currentUserID)) {
                        DataSnapshot commentsSnapshot = userSnapshot.child("comments");

                        // Iterate over all comments for the matched user to display
                        for (DataSnapshot commentSnapshot : commentsSnapshot.getChildren()) {
                            String username = commentSnapshot.child("username").getValue(String.class);
                            String content = commentSnapshot.child("content").getValue(String.class);
                            String postID = commentSnapshot.child("postId").getValue(String.class);
                            String commentID = commentSnapshot.getKey();

                            Object ratingObj = commentSnapshot.child("rating").getValue();
                            String rating = null;

                            if (ratingObj instanceof Long) {
                                rating = String.valueOf(ratingObj);
                            } else if (ratingObj instanceof String) {
                                rating = (String) ratingObj;
                            }

                            // display the comment if all fields are valid
                            if (username != null && content != null && rating != null) {
                                addCommentToLayout(commentID, username, content, rating, postID);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileComments.this, "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add each comment to the page for display
    private void addCommentToLayout(String commentId, String username, String content, String rating, String postID) {
        // code to add the comments to the page dynamically
        TextView usernameTextView = new TextView(this);
        usernameTextView.setText("Username: " + username);
        usernameTextView.setTextSize(20);
        usernameTextView.setPadding(0, 16, 0, 4);

        TextView ratingTextView = new TextView(this);
        ratingTextView.setText("Rating: " + rating);
        ratingTextView.setTextSize(20);
        ratingTextView.setPadding(0, 4, 0, 4);

        TextView contentTextView = new TextView(this);
        contentTextView.setText("Content: " + content);
        contentTextView.setTextSize(20);
        contentTextView.setPadding(0, 4, 0, 16);

        LinearLayout commentLayout = new LinearLayout(this);
        commentLayout.setOrientation(LinearLayout.VERTICAL);
        commentLayout.setPadding(0, 16, 0, 16);
        commentLayout.addView(usernameTextView);
        commentLayout.addView(ratingTextView);
        commentLayout.addView(contentTextView);

        // Calls modifier to delete/update comment
        commentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileComments.this, CommentModifier.class);
            intent.putExtra("commentId", commentId);
            intent.putExtra("postId", postID);
            intent.putExtra("username", username);
            intent.putExtra("content", content);
            intent.putExtra("rating", rating);
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        linearLayoutUserComments.addView(commentLayout);
    }
}
