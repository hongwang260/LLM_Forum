package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentModifier extends AppCompatActivity {

    private EditText editTextContent, editTextRating;
    private Button buttonSubmitComment;
    private String postId, postOwnerId, userId, username;
    private DatabaseReference commentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Initialize views
        editTextContent = findViewById(R.id.editTextContent);
        editTextRating = findViewById(R.id.editTextRating);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);

        // Retrieve postId, postOwnerId, and username from intent and UserSession
        postId = getIntent().getStringExtra("postId");
        postOwnerId = getIntent().getStringExtra("ownerId");

        // Initialize Firebase reference for comments under the specific post
        commentsRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

        // Retrieve the current user's ID from UserSession and find the username
        findUserDetails();

        // Set button click listener to submit comment
        buttonSubmitComment.setOnClickListener(v -> submitComment());
    }

    private void findUserDetails() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    if (user != null && user.ID.equals(UserSession.getInstance().getUserProfile().ID)) {
                        userId = user.ID; // This is the Firebase key for the user
                        username = user.username;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentModifier.this, "Failed to find user.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitComment() {
        // Retrieve content and rating inputs
        String content = editTextContent.getText().toString().trim();
        String ratingText = editTextRating.getText().toString().trim();

        if (ratingText.isEmpty()) {
            Toast.makeText(this, "Please provide content and rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            content = "user did not leave a content for this comment";
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Rating must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique comment ID
        String commentId = commentsRef.push().getKey();
        if (commentId == null) {
            Toast.makeText(this, "Failed to generate comment ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Comment object
        Comment comment = new Comment(commentId, userId, content, rating, username, postId);

        // Save the comment under the post in Firebase
        commentsRef.child(commentId).setValue(comment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentModifier.this, "Comment added.", Toast.LENGTH_SHORT).show();
                        saveCommentToUserProfile(comment);
                        finish(); // Navigate back to PostDetail activity
                    } else {
                        Toast.makeText(CommentModifier.this, "Failed to add comment.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveCommentToUserProfile(Comment comment) {
        // Reference to the "users" node to find the correct user by userId
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Loop through each user to find the one with a matching userId
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    UserProfile loggedInUser = UserSession.getInstance().getUserProfile();
                    Log.d("save", "post owner id " + postOwnerId);
                    if (user != null && user.ID.equals(loggedInUser.ID)) {
                        String userKey = userSnapshot.getKey();

                        DatabaseReference userCommentsRef = usersRef.child(userKey).child("comments").child(comment.commentId);

                        // Only save required fields in the user's comments section
                        Comment userComment = new Comment(comment.commentId, loggedInUser.ID, comment.content, comment.rating, comment.username, comment.postId);
                        userCommentsRef.setValue(userComment);

                        // if the person who made the post and the post owner is the same
                        // also need to store the comment to the current person's post comment section
                        if (user.ID.equals(postOwnerId)) {
                            DatabaseReference userPostRef = usersRef.child(userKey).child("posts").child(comment.postId).child("comments").child(comment.commentId);
                            userPostRef.setValue(userComment);
                        }
                    } else if (user != null && user.ID.equals(postOwnerId)) {
                        Log.d("save", "Entered save comment");
                        String userKey = userSnapshot.getKey();

                        DatabaseReference userCommentsRef = usersRef.child(userKey).child("posts").child(comment.postId).child("comments").child(comment.commentId);

                        // Only save required fields in the post owner's post comments section
                        Comment userComment = new Comment(comment.commentId, loggedInUser.ID, comment.content, comment.rating, comment.username, comment.postId);
                        userCommentsRef.setValue(userComment);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentModifier.this, "Failed to find user for comment.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
