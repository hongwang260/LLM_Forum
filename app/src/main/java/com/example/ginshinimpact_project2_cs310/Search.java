package com.example.ginshinimpact_project2_cs310;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Search extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private LinearLayout linearLayoutPosts;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search);

        Intent intent = getIntent();
        String key = intent.getStringExtra("Content");
        String filter = intent.getStringExtra("Option");
        databaseRef = FirebaseDatabase.getInstance().getReference("posts");
        linearLayoutPosts = findViewById(R.id.result);
        SearchPost(key, filter);
    }

    private void AddResult (String title, String llm, String postId, String content, String authorNote, String ownerId){
        LinearLayout result = new LinearLayout(this);
        result.setOrientation(LinearLayout.VERTICAL);
        result.setPadding(0, 16, 0, 16);
        result.setClickable(true);  // Make it clickable

        TextView titleTextView = new TextView(this);
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleTextView.setText(title);
        titleTextView.setTextSize(25);
        titleTextView.setPadding(0, 0, 0, 4);

        // Create a TextView for the LLM model kind
        TextView llmKindTextView = new TextView(this);
        llmKindTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        llmKindTextView.setText("Model: " + llm);
        llmKindTextView.setTextSize(16);
        llmKindTextView.setPadding(0, 4, 0, 16);

        result.addView(titleTextView);
        result.addView(llmKindTextView);

        result.setOnClickListener(v ->{
            Intent intent = new Intent(this, PostDetail.class);
            intent.putExtra("title", title);
            intent.putExtra("postId", postId);
            intent.putExtra("llmKind", llm);
            intent.putExtra("content", content);
            intent.putExtra("authorNotes", authorNote);
            intent.putExtra("ownerId", ownerId);
            startActivity(intent);
        });

        linearLayoutPosts.addView(result);
    }



    private void SearchPost(String key, String option) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        //data that is stored in the database, used to keep track of session
                        String llm = childSnapshot.child("llmKind").getValue(String.class);
                        String title = childSnapshot.child("title").getValue(String.class);
                        String content = childSnapshot.child("content").getValue(String.class);
                        String postId = childSnapshot.getKey();
                        String postOwner = childSnapshot.child("ownerId").getValue(String.class);
                        String authorNote = childSnapshot.child("authorNotes").getValue(String.class);

                        if (option.contains("LLM")) {
                            if (llm.contains(key)) {
                                AddResult(title, llm, postId, content, authorNote, postOwner);
                            }
                        } else if (option.contains("Titles")) {
                            if (title.contains(key)) {
                                AddResult(title, llm, postId, content, authorNote, postOwner);
                            }
                        } else {
                            if (content.contains(key) || title.contains(key) || authorNote.contains(key)) {
                                AddResult(title, llm, postId, content, authorNote, postOwner);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Search.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}