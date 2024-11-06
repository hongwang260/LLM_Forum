package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {
    private LinearLayout linearLayoutPosts;
    private DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        linearLayoutPosts = findViewById(R.id.linearLayoutPosts);
//        Button buttonCreatePost = findViewById(R.id.buttonCreatePost);

        // Initialize Firebase reference to "posts" node
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        // Load posts from Firebase
        loadPostsFromFirebase();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set listener for navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_my_posts) {
                startActivity(new Intent(HomePage.this, ProfilePosts.class));
                return true;
            } else if (id == R.id.nav_new_post) {
                startActivity(new Intent(HomePage.this, NewPostActivity.class));
                return true;
            } else if (id == R.id.nav_my_comments) {
                startActivity(new Intent(HomePage.this, ProfileComments.class));
                return true;
            } else if (id == R.id.nav_user_profile) {
                startActivity(new Intent(HomePage.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }


//    // Set OnClickListener to open NewPostActivity
//        buttonCreatePost.setOnClickListener(v -> {
//            Intent intent = new Intent(HomePage.this, NewPostActivity.class);
//            startActivity(intent);
//        });
//    }

    private void loadPostsFromFirebase() {
        databasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear any existing views in the LinearLayout
                linearLayoutPosts.removeAllViews();

                // Iterate over all posts and add them to the LinearLayout
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String title = postSnapshot.child("title").getValue(String.class);
                    String llmKind = postSnapshot.child("llmKind").getValue(String.class);
                    String content = postSnapshot.child("content").getValue(String.class);
                    String authorNotes = postSnapshot.child("authorNotes").getValue(String.class);
                    String postId = postSnapshot.getKey();

                    if (title != null && llmKind != null && content != null && authorNotes != null) {
                        addPostToLayout(postId, title, llmKind, content, authorNotes);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomePage.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToLayout(String postId, String title, String llmKind, String content, String authorNotes) {
        // Create a LinearLayout for each post
        LinearLayout postLayout = new LinearLayout(this);
        postLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        postLayout.setOrientation(LinearLayout.VERTICAL);
        postLayout.setPadding(0, 16, 0, 16);
        postLayout.setClickable(true);  // Make it clickable

        // Create a TextView for the post title
        TextView titleTextView = new TextView(this);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleTextView.setText(title);
        titleTextView.setTextSize(25);
        titleTextView.setPadding(0, 0, 0, 4);

        // Create a TextView for the LLM model kind
        TextView llmKindTextView = new TextView(this);
        llmKindTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        llmKindTextView.setText("Model: " + llmKind);
        llmKindTextView.setTextSize(16);
        llmKindTextView.setPadding(0, 4, 0, 16);

        // Add TextViews to the post layout
        postLayout.addView(titleTextView);
        postLayout.addView(llmKindTextView);

        // Set onClickListener to open PostDetail with the full post details
        postLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, PostDetail.class);
            intent.putExtra("title", title);
            intent.putExtra("llmKind", llmKind);
            intent.putExtra("content", content);
            intent.putExtra("authorNotes", authorNotes);
            intent.putExtra("postId", postId);
            startActivity(intent);
        });

        // Add the post layout to the main LinearLayout
        linearLayoutPosts.addView(postLayout);
    }
}
