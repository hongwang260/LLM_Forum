package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
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

        linearLayoutUserPosts = findViewById(R.id.linearLayoutUserPosts);
        currentUserID = UserSession.getInstance().getUserProfile().ID;
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Load the current user's posts
        loadUserPosts();
    }

    // used to load the posts for this user
    private void loadUserPosts() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayoutUserPosts.removeAllViews();

                // Loop through all users to find the one with the matching ID
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userID = userSnapshot.child("ID").getValue(String.class);

                    if (userID != null && userID.equals(currentUserID)) {
                        DataSnapshot postsSnapshot = userSnapshot.child("posts");

                        // Iterate over all posts for the matched user
                        for (DataSnapshot postSnapshot : postsSnapshot.getChildren()) {
                            String title = postSnapshot.child("title").getValue(String.class);
                            String llmKind = postSnapshot.child("llmKind").getValue(String.class);
                            String postId = postSnapshot.getKey();
                            String content = postSnapshot.child("content").getValue(String.class);  // Content of the post
                            String authorNotes = postSnapshot.child("authorNotes").getValue(String.class);  // Author notes
                            String postOwner = postSnapshot.child("ownerId").getValue(String.class);

                            if (title != null && llmKind != null) {
                                addPostToLayout(postId, title, llmKind, content, authorNotes, postOwner);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfilePosts.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add each post to the page for display
    private void addPostToLayout(String postId, String title, String llmKind, String content, String authorNotes, String postOwner) {
        // Create a LinearLayout for each post
        LinearLayout postLayout = new LinearLayout(this);
        postLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        postLayout.setOrientation(LinearLayout.VERTICAL);
        postLayout.setPadding(0, 8, 0, 8);
        postLayout.setClickable(true);  // Make it clickable

        TextView titleTextView = new TextView(this);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleTextView.setText("Title: " + title);
        titleTextView.setTextSize(20);
        titleTextView.setPadding(0, 0, 0, 4);

        TextView llmKindTextView = new TextView(this);
        llmKindTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        llmKindTextView.setText("Model: " + llmKind);
        llmKindTextView.setTextSize(20);
        llmKindTextView.setPadding(0, 4, 0, 8);

        postLayout.addView(titleTextView);
        postLayout.addView(llmKindTextView);

        // used to show the detail page of the post
        postLayout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePosts.this, PostDetail.class);
            intent.putExtra("title", title);
            intent.putExtra("llmKind", llmKind);
            intent.putExtra("content", content);
            intent.putExtra("authorNotes", authorNotes);
            intent.putExtra("postId", postId);
            intent.putExtra("ownerId", postOwner);
            startActivity(intent);
        });

        linearLayoutUserPosts.addView(postLayout);
    }
}
