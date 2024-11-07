package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostModifier extends AppCompatActivity {

    private EditText editTextTitle, editTextContent, editTextAuthorNotes, editTextLLMKind;    private Button buttonSavePost;
    private String postId;
    private DatabaseReference postRef;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_modifier);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        editTextAuthorNotes = findViewById(R.id.editTextAuthorNotes);
        buttonSavePost = findViewById(R.id.buttonSavePost);
        editTextLLMKind = findViewById(R.id.editTextLLMKind);

        postId = getIntent().getStringExtra("postId");
        postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loadPostData();

        buttonSavePost.setOnClickListener(v -> updatePost());
    }

    private void loadPostData() {
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    editTextTitle.setText(post.title);
                    editTextContent.setText(post.content);
                    editTextAuthorNotes.setText(post.authorNotes);
                    editTextLLMKind.setText(post.llmKind);  // Load LLM Model
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostModifier.this, "Failed to load post data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePost() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String authorNotes = editTextAuthorNotes.getText().toString().trim();
        String llmKind = editTextLLMKind.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty() || llmKind.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // this updates the posts root node foe the modify
        postRef.child("title").setValue(title);
        postRef.child("content").setValue(content);
        postRef.child("authorNotes").setValue(authorNotes);
        postRef.child("llmKind").setValue(llmKind)  // Update LLM Model
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PostModifier.this, "Post updated.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PostModifier.this, "Failed to update post.", Toast.LENGTH_SHORT).show();
                    }
                });

        // save the update correctly in the users root node
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop through each user node to find the correct user by ID
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserProfile user = userSnapshot.getValue(UserProfile.class);
                    UserProfile loggedInUser = UserSession.getInstance().getUserProfile();
                    if (user != null && user.ID.equals(loggedInUser.ID)) {
                        String userKey = userSnapshot.getKey();

                        // Reference to the user's posts section
                        DatabaseReference userPostsRef = usersRef.child(userKey).child("posts").child(postId);
                        userPostsRef.child("title").setValue(title);
                        userPostsRef.child("content").setValue(content);
                        userPostsRef.child("authorNotes").setValue(authorNotes);
                        userPostsRef.child("llmKind").setValue(llmKind);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PostModifier.this, "Failed to access users in database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}