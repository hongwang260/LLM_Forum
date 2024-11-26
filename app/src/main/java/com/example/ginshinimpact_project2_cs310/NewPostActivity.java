package com.example.ginshinimpact_project2_cs310;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class NewPostActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextLLMKind, editTextContent, editTextAuthorNotes;
    private Button buttonSavePost;
    private DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        databasePosts = FirebaseDatabase.getInstance().getReference("posts");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextLLMKind = findViewById(R.id.editTextLLMKind);
        editTextContent = findViewById(R.id.editTextContent);
        editTextAuthorNotes = findViewById(R.id.editTextAuthorNotes);
        buttonSavePost = findViewById(R.id.buttonSavePost);

        buttonSavePost.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        // Get the current user that is logged in
        UserProfile userProfile = UserSession.getInstance().getUserProfile();

        // input data
        String title = editTextTitle.getText().toString().trim();
        String llmKind = editTextLLMKind.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();
        String authorNotes = editTextAuthorNotes.getText().toString().trim();

        // check required fields
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(llmKind) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique ID for the post
        String postId = databasePosts.push().getKey();

        Post post = new Post(postId, title, llmKind, content, authorNotes, userProfile.getID());

        // Save the post to database
        if (postId != null) {
            // Save to global posts node
            databasePosts.child(postId).setValue(post);
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // save the post to the correct user's posts
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Loop through each user node to find the correct user by ID
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        UserProfile user = userSnapshot.getValue(UserProfile.class);
                        if (user != null && user.getID().equals(userProfile.getID())) {
                            String userKey = userSnapshot.getKey();

                            // Reference to the user's posts section
                            DatabaseReference userPostsRef = usersRef.child(userKey).child("posts").child(postId);
                            userPostsRef.setValue(post).addOnCompleteListener(userTask -> {
                                if (userTask.isSuccessful()) {
                                    Toast.makeText(NewPostActivity.this, "Post saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(NewPostActivity.this, "Failed to save post under user", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(NewPostActivity.this, "Failed to access users in database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
