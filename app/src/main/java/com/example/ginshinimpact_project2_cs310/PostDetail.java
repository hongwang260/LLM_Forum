package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetail extends AppCompatActivity {

    private LinearLayout linearLayoutComments;
    private DatabaseReference postRef;
    private String postId, postOwnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        // Retrieve post details passed from the HomePage activity
        postId = getIntent().getStringExtra("postId");
        postOwnerId = getIntent().getStringExtra("ownerId");
        String title = getIntent().getStringExtra("title");
        String llmKind = "Model: " + getIntent().getStringExtra("llmKind");
        String content = "Content: " + getIntent().getStringExtra("content");
        String authorNotes = "Author Notes: " + getIntent().getStringExtra("authorNotes");

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        Button addCommentButton = findViewById(R.id.buttonAddComment);
        addCommentButton.setOnClickListener(v -> {
            Intent intent = new Intent(PostDetail.this, CommentModifier.class);
            intent.putExtra("postId", postId);
            intent.putExtra("ownerId", postOwnerId);
            startActivity(intent);
        });

        Button modifyPostButton = findViewById(R.id.buttonModifyPost);
        Button deletePostButton = findViewById(R.id.buttonDeletePost);

        modifyPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(PostDetail.this, PostModifier.class);
            intent.putExtra("postId", postId);
            startActivity(intent);
        });

        deletePostButton.setOnClickListener(v -> deletePost());

        TextView titleTextView = findViewById(R.id.textViewTitle);
        TextView llmKindTextView = findViewById(R.id.textViewLLMKind);
        TextView contentTextView = findViewById(R.id.textViewContent);
        TextView authorNotesTextView = findViewById(R.id.textViewAuthorNotes);
        titleTextView.setText(title);
        llmKindTextView.setText(llmKind);
        contentTextView.setText(content);
        authorNotesTextView.setText(authorNotes);

        linearLayoutComments = findViewById(R.id.linearLayoutComments);
        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

        // Load comments with real-time updates
        loadComments();
    }

    private void loadComments() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutComments.removeAllViews();

                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String username = commentSnapshot.child("username").getValue(String.class);
                    String content = commentSnapshot.child("content").getValue(String.class);

                    Object ratingObj = commentSnapshot.child("rating").getValue();
                    String rating = null;

                    if (ratingObj instanceof Long) {
                        rating = String.valueOf(ratingObj); // Convert Long to String
                    } else if (ratingObj instanceof String) {
                        rating = (String) ratingObj; // Use String directly
                    }

                    if (username != null && content != null && rating != null) {
                        addCommentToLayout(username, content, rating);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostDetail.this, "Failed to load comments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCommentToLayout(String username, String content, String rating) {
        TextView usernameTextView = new TextView(this);
        usernameTextView.setText(username + ": " + content);
        usernameTextView.setTextSize(16);
        usernameTextView.setPadding(0, 16, 0, 4);

        TextView ratingTextView = new TextView(this);
        ratingTextView.setText("Rating: " + rating);
        ratingTextView.setTextSize(14);
        ratingTextView.setPadding(0, 4, 0, 4);

        linearLayoutComments.addView(usernameTextView);
        linearLayoutComments.addView(ratingTextView);
    }



    private void deletePost() {
        // Remove the post from the "posts" root
        FirebaseDatabase.getInstance().getReference("posts").child(postId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PostDetail.this, "Post deleted successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PostDetail.this, "Failed to delete post.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Remove associated comments from each user's profile
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userKey = userSnapshot.getKey();
                    DatabaseReference userCommentsRef = usersRef.child(userKey).child("comments");

                    userCommentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot commentSnapshot) {
                            for (DataSnapshot comment : commentSnapshot.getChildren()) {
                                String associatedPostId = comment.child("postId").getValue(String.class);
                                if (postId.equals(associatedPostId)) {
                                    comment.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(PostDetail.this, "Failed to remove comments associated with post.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostDetail.this, "Failed to fetch user data for comment deletion.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
