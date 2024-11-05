package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPostActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextLLMKind, editTextContent, editTextAuthorNotes;
    private Button buttonSavePost;
    private DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Initialize Firebase reference
        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        // Initialize fields
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextLLMKind = findViewById(R.id.editTextLLMKind);
        editTextContent = findViewById(R.id.editTextContent);
        editTextAuthorNotes = findViewById(R.id.editTextAuthorNotes);
        buttonSavePost = findViewById(R.id.buttonSavePost);

        // Set click listener for save button
        buttonSavePost.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        //get the current user that is logged in
        UserProfile userProfile = UserSession.getInstance().getUserProfile();

        String title = editTextTitle.getText().toString().trim();
        String llmKind = editTextLLMKind.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String authorNotes = editTextAuthorNotes.getText().toString().trim();

        // Validate required fields
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(llmKind) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique ID for post
        String postId = databasePosts.push().getKey();

        // Create a new post object
        Post post = new Post(postId, title, llmKind, content, authorNotes, userProfile.ID);

        // Save the post to Firebase
        if (postId != null) {
            databasePosts.child(postId).setValue(post)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(NewPostActivity.this, "Post saved", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NewPostActivity.this, "Failed to save post", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}