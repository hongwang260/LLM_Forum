package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetail extends AppCompatActivity {

    private LinearLayout linearLayoutComments;
    private DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        // Retrieve post details passed from the HomePage activity
        String title = getIntent().getStringExtra("title");
        String postId = getIntent().getStringExtra("postId");
        String llmKind = "Model: " + getIntent().getStringExtra("llmKind");
        String content = "Content: " + getIntent().getStringExtra("content");
        String authorNotes = "Author Notes: " + getIntent().getStringExtra("authorNotes");

        TextView titleTextView = findViewById(R.id.textViewTitle);
        TextView llmKindTextView = findViewById(R.id.textViewLLMKind);
        TextView contentTextView = findViewById(R.id.textViewContent);
        TextView authorNotesTextView = findViewById(R.id.textViewAuthorNotes);
        titleTextView.setText(title);
        llmKindTextView.setText(llmKind);
        contentTextView.setText(content);
        authorNotesTextView.setText(authorNotes);

        // Initialize comments layout and Firebase reference
        linearLayoutComments = findViewById(R.id.linearLayoutComments);
        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments");

        // Load comments from Firebase
        loadComments();
    }

    //load the comments from database for display
    private void loadComments() {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutComments.removeAllViews();  // Clear previous views
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    String username = commentSnapshot.child("username").getValue(String.class);
                    String content = commentSnapshot.child("content").getValue(String.class);
                    //if the user did not provide a content for the comment
                    if (content == null) {
                        content = "user only left a rating";
                    }
                    String rating = commentSnapshot.child("rating").getValue(String.class);

                    //display only if the rating and username is not null
                    if (username != null && rating != null) {
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

    //add comments to the screeen on the detail page for the post
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
}
