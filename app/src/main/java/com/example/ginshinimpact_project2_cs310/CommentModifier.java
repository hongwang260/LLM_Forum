package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentModifier extends AppCompatActivity {

    private EditText editTextContent, editTextRating;
    private Button buttonSubmitComment, buttonDeleteComment;
    private String postId, postOwnerId, userId, username, commentId;
    private DatabaseReference commentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Initialize views
        editTextContent = findViewById(R.id.editTextContent);
        editTextRating = findViewById(R.id.editTextRating);
        buttonSubmitComment = findViewById(R.id.buttonSubmitComment);
        buttonDeleteComment = findViewById(R.id.buttonDeleteComment);

        // Retrieve postId, postOwnerId, and commentId (if editing an existing comment)
        postId = getIntent().getStringExtra("postId");
        postOwnerId = getIntent().getStringExtra("ownerId");
        commentId = getIntent().getStringExtra("commentId");

        // Initialize Firebase reference for comments under the specific post
        commentsRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

        // Retrieve the current user's ID and username from UserSession
        findUserDetails();

        // If editing an existing comment, load its data and show the delete button
        if (commentId != null) {
            loadExistingCommentData();
            buttonDeleteComment.setVisibility(View.VISIBLE); // Show delete button for existing comments
        }

        // Set button click listeners
        buttonSubmitComment.setOnClickListener(v -> submitOrUpdateComment());
        buttonDeleteComment.setOnClickListener(v -> deleteComment());
    }

    private void findUserDetails() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    if (user != null && user.ID.equals(UserSession.getInstance().getUserProfile().ID)) {
                        userId = user.ID;
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

    private void loadExistingCommentData() {
        commentsRef.child(commentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null) {
                    editTextContent.setText(comment.content);
                    editTextRating.setText(String.valueOf(comment.rating));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentModifier.this, "Failed to load comment data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitOrUpdateComment() {
        // Retrieve content and rating inputs
        String content = editTextContent.getText().toString().trim();
        String ratingText = editTextRating.getText().toString().trim();

        if (ratingText.isEmpty()) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            content = "User did not leave content for this comment.";
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Rating must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use existing commentId if updating, otherwise create a new one
        if (commentId == null) {
            commentId = commentsRef.push().getKey();
            if (commentId == null) {
                Toast.makeText(this, "Failed to generate comment ID.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create or update the Comment object
        Comment comment = new Comment(commentId, userId, content, rating, username, postId);

        // Save or update the comment under the post in Firebase
        commentsRef.child(commentId).setValue(comment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentModifier.this, "Comment saved.", Toast.LENGTH_SHORT).show();
                        saveOrUpdateCommentInUserProfile(comment);
                        finish(); // Navigate back to PostDetail activity
                    } else {
                        Toast.makeText(CommentModifier.this, "Failed to save comment.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveOrUpdateCommentInUserProfile(Comment comment) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // loop to find the user that is logged in
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    UserProfile loggedInUser = UserSession.getInstance().getUserProfile();

                    if (user != null) {
                        String userKey = userSnapshot.getKey();
                        // Update or save comment in the current user's comments section
                        if (user.ID.equals(loggedInUser.ID)) {
                            DatabaseReference userCommentsRef = usersRef.child(userKey).child("comments").child(comment.commentId);
                            Comment userComment = new Comment(comment.commentId, loggedInUser.ID, comment.content, comment.rating, comment.username, comment.postId);
                            userCommentsRef.setValue(userComment);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentModifier.this, "Failed to find user for comment to save.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteComment() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // loop to find the user that is logged in
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    UserProfile loggedInUser = UserSession.getInstance().getUserProfile();

                    if (user != null) {
                        String userKey = userSnapshot.getKey();
                        // remove the comment from the user comments section
                        if (user.ID.equals(loggedInUser.ID)) {
                            usersRef.child(userKey).child("comments").child(commentId).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentModifier.this, "Failed to find user for comment to remove.", Toast.LENGTH_SHORT).show();
            }
        });

        // remove the comment in the posts root
        commentsRef.child(commentId).removeValue();

        Toast.makeText(CommentModifier.this, "Comment deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
